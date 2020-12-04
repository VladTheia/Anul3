"""
This module represents the Producer.

Computer Systems Architecture Course
Assignment 1
March 2020
"""

from threading import Thread
from time import sleep


class Producer(Thread):
    """
    Class that represents a producer.
    """

    def __init__(self, products, marketplace, republish_wait_time, **kwargs):
        """
        Constructor.

        @type products: List()
        @param products: a list of products that the producer will produce

        @type marketplace: Marketplace
        @param marketplace: a reference to the marketplace

        @type republish_wait_time: Time
        @param republish_wait_time: the number of seconds that a producer must
        wait until the marketplace becomes available

        @type kwargs:
        @param kwargs: other arguments that are passed to the Thread's __init__()
        """
        Thread.__init__(self)
        self.products = products
        self.marketplace = marketplace
        self.republish_wait_time = republish_wait_time
        # inregistram producatorul la momentul instantierii
        self.id = self.marketplace.register_producer()
        self.name = kwargs['name']
        self.setDaemon(kwargs['daemon'])

    def run(self):
        while True:
            # iteram prin produsele producatorului
            for product in self.products:
                # publicam un produs de cate ori este indicat
                for _ in range(product[1]):
                    if not self.marketplace.publish(self.id, product[0]):
                        # daca lista pentru acest producator este plina, asteptam
                        sleep(self.republish_wait_time)
                        continue
                    # daca produsul a fost publicat cu succes asteptam cat este indicat
                    sleep(product[2])
