import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * Class that implements the channel used by headquarters and space explorers to communicate.
 */
public class CommunicationChannel {
    // we're making a channel with 2 lists, allowing the 2 threads to operate in the same time
    public Queue<Message> messagesForExp; // HQs send and explorers receive
    public Queue<Message> messagesForHq; // explorers send and HQs receive
    // semaphores for each action, thus preventing reading from a list, while someone is still writing in it or vice versa
    Semaphore semHqChannelLockedChunk;
    public Semaphore semHqGet;
    public Semaphore semHqPut;
    Semaphore semExpChannelLock;
    Semaphore semNull;
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
        semHqGet = new Semaphore(1);
        semHqPut = new Semaphore(1);
        semExpChannelLock = new Semaphore(1);
        semNull = new Semaphore(0);
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
            // avoid getting messages while putting
            semExpChannelLock.acquire();
            messagesForHq.add(message);
            semNull.release(); // the semaphore is released for every added message
            semExpChannelLock.release();
        } catch (InterruptedException e) {}
    }

    /**
     * Gets a message from the space explorer channel (i.e., where space explorers write to and
     * headquarters read from).
     *
     * @return message from the space explorer channel
     */
    public Message getMessageSpaceExplorerChannel() {
        try {
            // avoid putting messages while getting
            semExpChannelLock.acquire();
            if (messagesForHq.isEmpty()) {
                semExpChannelLock.release();
                semNull.acquire(); // the semaphore is acquired for every received message
                semExpChannelLock.acquire();
            }
            Message message = messagesForHq.poll();
            semExpChannelLock.release();
            return message;
        } catch (InterruptedException e) {}
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
            semHqPut.acquire();
            // the first thread to send the first message in a chunk locks the chunk
            if (firstFromChunk) {
                semHqChannelLockedChunk.acquire();
                firstFromChunk = false;
                expectedHqId = Thread.currentThread().getId();
                // the other threads wait for the chunk to finish being sent
            } else if (Thread.currentThread().getId() != expectedHqId) {
                semHqPut.release();
                // other threads wait here until the working thread finishes putting the chunk,
                semHqChannelLockedChunk.acquire();
                semHqPut.acquire();
                firstFromChunk = false;
                expectedHqId = Thread.currentThread().getId();
            }

            //if it's an END/EXIT message, the chunk is released
            if (message.getData().equals(HeadQuarter.END) || message.getData().equals(HeadQuarter.EXIT)) {
                messagesForExp.add(message);
                firstFromChunk = true;
                expectedHqId = -1;
                semHqChannelLockedChunk.release();
                semHqPut.release();
            } else {
                // it's a regular message
                messagesForExp.add(message);
                semHqPut.release();
            }
        } catch (InterruptedException e) {}
    }

    /**
     * Gets a message from the headquarters channel (i.e., where headquarters write to and
     * space explorer read from).
     *
     * @return message from the header quarter channel
     */
    public Message getMessageHeadQuarterChannel() {
        /*
         * This method is oriented on locking a pair of parent-child messages, so if it's an END/EXIT message we release the semaphores
         * and if it's a parent message, we're sure there is a child message so we wait the it until the child is received by
         * the thread that got its parent
         */
        try {
            // lock the message so other explorers can't change the contents of messagesForExp
            semHqGet.acquire();
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
                            semHqGet.release();
                            return message;
                        } else {
                            // it's a parent message, we keep the chunk locked so we can get the child node
                            semHqGet.release();
                            return message;
                        }
                    } else {
                        // if the messages list is empty we release the chunk and message lock
                        semHqChannelLockedChunk.release();
                        semHqGet.release();
                        return null;
                    }
                    // the other threads wait for the chunk to finish being sent
                } else if (Thread.currentThread().getId() != expectedExpId) {
                    // here is where the other explorer threads wait it there is another one already getting a pair of nodes
                    semHqGet.release();
                    semHqChannelLockedChunk.acquire(); // the other threads stop here
                    // after a thread acquires the semaphore, it behaves like it was the first to get to it and follows
                    // the same algorithm
                    semHqGet.acquire();
                    firstFromPair = false;
                    expectedExpId = Thread.currentThread().getId();
                    if (!messagesForExp.isEmpty()) {
                        Message message = messagesForExp.poll();
                        // if it's an END/EXIT message, it wasn't a pair so we release the message and the chunk
                        if (message.getData().equals(HeadQuarter.END) || message.getData().equals(HeadQuarter.EXIT)) {
                            firstFromPair = true;
                            expectedExpId = -1;
                            semHqChannelLockedChunk.release();
                            semHqGet.release();
                            return message;
                        } else {
                            // it's a parent message, we keep the chunk locked so we can get the child node
                            semHqGet.release();
                            return message;
                        }
                    } else {
                        semHqChannelLockedChunk.release();
                        semHqGet.release();
                        // if the messages list is empty we release the chunk and message lock
                        return null;
                    }
                }
                // if it's not an END/EXIT message or the parent (hence it's not first from pair), it means it's a child node message
                Message message = messagesForExp.poll();
                firstFromPair = true;
                expectedExpId = -1;
                // the pair is received, so we can release the chunk in order for other explorers to receive from it
                // or others HQs to put new chunks in it
                semHqChannelLockedChunk.release();
                semHqGet.release();
                return message;
            }
            // if we're here it means the message was empty
            semHqGet.release();
        } catch (InterruptedException e) {}
        return null;
    }
}

