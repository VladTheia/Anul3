import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * Class that implements the channel used by headquarters and space explorers to communicate.
 */
public class CommunicationChannel {
	// we're making a channel with 2 lists, allowing the 2 threads to operate in the same time
	Queue<Message> messagesForExp; // HQs send and explorers receive
	Queue<Message> messagesForHq; // explorers send and HQs receive
	// semaphores for each action, thus preventing reading from a list, while someone is still writing in it or vice versa
    Semaphore semHqChannelLockedChunk;
    Semaphore semLockMessage;
    Semaphore semLockMessage2;
    Semaphore semExpChannelLock;
	//
    long expectedHqId;
    long expectedExpId;
    boolean firstFromChunk;
    boolean firstFromPair;

	/**
	 * Creates a {@code CommunicationChannel} object.
	 */
	public CommunicationChannel() {
		messagesForExp = new LinkedList<Message>();
		messagesForHq = new LinkedList<Message>();
        semHqChannelLockedChunk = new Semaphore(1);
        semLockMessage = new Semaphore(1);
        semLockMessage2 = new Semaphore(1);
        semExpChannelLock = new Semaphore(1);
		firstFromChunk = true;
		firstFromPair = true;
		expectedHqId = -1;
        expectedExpId = -1;
	}

	/**
	 * Puts a message on the space explorer channel (i.e., where space explorers write to and 
	 * headquarters read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageSpaceExplorerChannel(Message message) {
        try {
            semExpChannelLock.acquire();
            messagesForHq.add(message);
            semExpChannelLock.release();
        } catch (InterruptedException e) {

        }
    }

	/**
	 * Gets a message from the space explorer channel (i.e., where space explorers write to and
	 * headquarters read from).
	 * 
	 * @return message from the space explorer channel
	 */
	public Message getMessageSpaceExplorerChannel() {
        try {
            semExpChannelLock.acquire();
            Message message = messagesForHq.poll();
            semExpChannelLock.release();
            return message;
        } catch (InterruptedException e) {
        }
        return null;
	}

	/**
	 * Puts a message on the headquarters channel (i.e., where headquarters write to and 
	 * space explorers read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageHeadQuarterChannel(Message message) {
	    /*
	     * Method is oriented on locking a chunk (from first message to END message) for a thread and releases it when
         * the thread is done putting all the messages from the chunk in the communication channel
	    */
		try {
		    semLockMessage2.acquire();
		    // the first thread to send the first message in a chunk locks the chunk
            if (firstFromChunk) {
                semHqChannelLockedChunk.acquire();
                firstFromChunk = false;
                expectedHqId = Thread.currentThread().getId();
            // the other threads wait for the chunk to finish being sent
            } else if (Thread.currentThread().getId() != expectedHqId) {
                semLockMessage2.release();
                // other threads wait here until the working thread finishes putting the chunk,
                semHqChannelLockedChunk.acquire();
                semLockMessage2.acquire();
                firstFromChunk = false;
                expectedHqId = Thread.currentThread().getId();
            }

            //if it's an END/EXIT message, the chunk is released
            if (message.getData().equals(HeadQuarter.END) || message.getData().equals(HeadQuarter.EXIT)) {
                messagesForExp.add(message);
                firstFromChunk = true;
                expectedHqId = -1;
                semHqChannelLockedChunk.release();
                semLockMessage2.release();
            } else {
                // it's a regular message
                messagesForExp.add(message);
                semLockMessage2.release();
            }
        } catch (InterruptedException e) {
		}
    }

	/**
	 * Gets a message from the headquarters channel (i.e., where headquarters write to and
	 * space explorer read from).
	 * 
	 * @return message from the header quarter channel
	 */
	public Message getMessageHeadQuarterChannel() {
        /*
         * This method is oriented on locking a pair of parent-child messages, so if it's an END/EXIT message we release the locks
         * and if it's a parent message, we're sure there is a child message so we lock the it until the child is received by
         * the thread that got its parent
         */
        try {
            // lock the message so other explorers can't change the contents of messagesForExp
            semLockMessage.acquire();
            // if the messages list is empty, we release the message and return null
            if (!messagesForExp.isEmpty()) {
                // the first thread to get the first message in a pair locks the chunk, so the HQs can't put any messages
                // until the pair is received
                if (firstFromPair) {
                    semHqChannelLockedChunk.acquire();
                    firstFromPair = false;
                    expectedExpId = Thread.currentThread().getId();
                    if (!messagesForExp.isEmpty()) {
                        Message message = messagesForExp.poll();
                        // if it's an END/EXIT message, it wasn't a pair so we release the message and the chunk
                        if (message.getData().equals(HeadQuarter.END) || message.getData().equals(HeadQuarter.EXIT)) {
                            firstFromPair = true;
                            expectedExpId = -1;
                            semHqChannelLockedChunk.release();
                            semLockMessage.release();
                            return message;
                        } else {
                            // it's a parent message, we keep the chunk locked so we can get the child node
                            semLockMessage.release();
                            return message;
                        }
                    } else {
                        // if the messages list is empty we release the chunk and message lock
                        semHqChannelLockedChunk.release();
                        semLockMessage.release();
                        return null;
                    }
                    // the other threads wait for the chunk to finish being sent
                } else if (Thread.currentThread().getId() != expectedExpId) {
                    // here is where the other explorer threads wait it there is another one already getting a pair of nodes
                    semLockMessage.release();
                    semHqChannelLockedChunk.acquire(); // aici se opresc celelalte threadyru
                    semLockMessage.acquire();
                    firstFromPair = false;
                    expectedExpId = Thread.currentThread().getId();
                    if (!messagesForExp.isEmpty()) {
                        Message message = messagesForExp.poll();
                        // if it's an END/EXIT message, it wasn't a pair so we release the message and the chunk
                        if (message.getData().equals(HeadQuarter.END) || message.getData().equals(HeadQuarter.EXIT)) {
                            firstFromPair = true;
                            expectedExpId = -1;
                            semHqChannelLockedChunk.release();
                            semLockMessage.release();
                            return message;
                        } else {
                            // it's a parent message, we keep the chunk locked so we can get the child node
                            semLockMessage.release();
                            return message;
                        }
                    } else {
                        semHqChannelLockedChunk.release();
                        semLockMessage.release();
                        // if the messages list is empty we release the chunk and message lock
                        return null;
                    }
                }
                // if it's not an END/EXIT message or the parent, it means it's a child node message
                Message message = messagesForExp.poll();
                firstFromPair = true;
                expectedExpId = -1;
                // the pair is received, so we can release the chunk in order for other explorers to receive from it
                // or others HQs to put new chunks in it
                semHqChannelLockedChunk.release();
                semLockMessage.release();
                return message;
            }
            // if we're here it means the message was empty
            semLockMessage.release();
        } catch (InterruptedException e) {
        }
        return null;
    }
}

