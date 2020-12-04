"""
This module represents the Consumer.

Computer Systems Architecture Course
Assignment 1
March 2020
"""

from threading import Thread
from time import sleep


class Consumer(Thread):
    """
    Class that represents a consumer.
    """

    def __init__(self, carts, marketplace, retry_wait_time, **kwargs):
        """
        Constructor.

        :type carts: List
        :param carts: a list of add and remove operations

        :type marketplace: Marketplace
        :param marketplace: a reference to the marketplace

        :type retry_wait_time: Time
        :param retry_wait_time: the number of seconds that a consumer must wait
        until the Marketplace becomes available

        :type kwargs:
        :param kwargs: other arguments that are passed to the Thread's __init__()
        """
        Thread.__init__(self)
        self.carts = carts
        self.marketplace = marketplace
        self.retry_wait_time = retry_wait_time
        self.name = kwargs['name']

    def run(self):
        # trecem prin toate cart-urile clientului
        for cart in self.carts:
            # obtinem id-ul fiecarui cart
            cart_id = self.marketplace.new_cart()
            #iteram prin actiunile indicate in fiecare cart
            for action in cart:
                # facem actiunea de 'quantity' ori
                for _ in range(action['quantity']):
                    if action['type'] == 'add':
                        while not self.marketplace.add_to_cart(cart_id, action['product']):
                            # asteptam si reincercam sa adaugam produsul pana reusim
                            sleep(self.retry_wait_time)
                    else:
                        # stergem produsul din cos si il adaugam in lista de unde a fost luat
                        self.marketplace.remove_from_cart(cart_id, action['product'])
            order = self.marketplace.place_order(cart_id)
            # facem reverse listei pentru a pastra ordinea cronologica
            order = reversed(order)
            for product in order:
                print(self.name + " bought " + str(product[1]))
