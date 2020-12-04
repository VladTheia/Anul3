"""
This module represents the Marketplace.

Computer Systems Architecture Course
Assignment 1
March 2020
"""
import threading


class Marketplace:
    """
    Class that represents the Marketplace. It's the central part of the implementation.
    The producers and consumers use its methods concurrently.
    """
    def __init__(self, queue_size_per_producer):
        """
        Constructor

        :type queue_size_per_producer: Int
        :param queue_size_per_producer: the maximum size of a queue associated with each producer
        """
        self.queue_size_per_prod = queue_size_per_producer
        self.stock = {}  # dictionar de liste pentru producatori (key: String - value: List)
        self.locks = {}  # dictionar de lock-uri pentru sincronizari (key: String - value: Lock)
        self.carts = []  # lista de liste pentru cosurile de cumparaturi
        self.prod_id = 1  # index pentru crearea string-ului care reprezinta id-ul producatorului
        self.cart_id = 0  # index pentru crearea id-ului unui cart

    def register_producer(self):
        """
        Returns an id for the producer that calls this.
        """
        prod_id = "prod" + str(self.prod_id)  # concatenare pentru obtinerea prod_id
        lock = threading.Lock()  # crearea cate unui lock pentru fiecare producator
        # inserarea unei liste goale in dictionarul de liste asociat key-ul 'prod_id'
        self.stock[prod_id] = []
        # inserarea unui lock in dictionarul de lock-uri pentru producatorul creat
        self.locks[prod_id] = lock
        self.prod_id += 1  # incrementarea index-ului pentru urmatorul producator creat
        return prod_id

    def publish(self, producer_id, product):
        """
        Adds the product provided by the producer to the marketplace

        :type producer_id: String
        :param producer_id: producer id

        :type product: Product
        :param product: the Product that will be published in the Marketplace

        :returns True or False. If the caller receives False, it should wait and then try again.
        """
        self.locks[producer_id].acquire()  # intrarea in zona thread-safe
        if self._producer_not_full(producer_id):  # daca mai este spatiu in lista acestui producator
            self.stock[producer_id].append(product)  # adaugam produsul corespodenta cheii primite
            result = True  # valoarea de retur a metodei este True daca reusim sa adaugam un produs
        else:
            result = False # daca nu, este False
        self.locks[producer_id].release()  # iesirea din zona thread-safe
        return result

    def new_cart(self):
        """
        Creates a new cart for the consumer

        :returns an int representing the cart_id
        """
        cart_id = self.cart_id  # cart_id este cel la care a ajuns index-ul in momentul de fata
        self.carts.append([])  # adaugam lista in lista de cart-uri la index-ul 'cart_id'
        self.cart_id += 1  # incrementam index-ul pentru urmatorul cart
        return cart_id

    def add_to_cart(self, cart_id, product):
        """
        Adds a product to the given cart. The method returns

        :type cart_id: Int
        :param cart_id: id cart

        :type product: Product
        :param product: the product to add to cart

        :returns True or False. If the caller receives False, it should wait and then try again
        """
        for key in self.stock:  # iteram prin cheile dictionarului de liste cu produse
            if product in self.stock[key]:  # iteram prin liste
                self.locks[key].acquire()  # intram in zona thread-safe
                self.stock[key].remove(product)  # scoate produsul din lista producatorului
                # adaugam produsul si prod_id-ul de unde provine in lista cart-ului curent
                self.carts[cart_id].append((key, product))
                self.locks[key].release()  # iesirea din zona thread-safe
                return True
        return False

    def remove_from_cart(self, cart_id, product):
        """
        Removes a product from cart.

        :type cart_id: Int
        :param cart_id: id cart

        :type product: Product
        :param product: the product to remove from cart;
        """
        # iteram prin lista de perechi a dictionarului de carturi, de la cheia cart_id
        for pair in self.carts[cart_id]:
            # daca produsul pe care vrem sa il scoatem din cart se afla in pereche
            if product in pair:
                self.locks[pair[0]].acquire()  # intram in zona thread -afe
                self.stock[pair[0]].append(product)  # adaugam produsul in lista din care provenea
                self.carts[cart_id].remove(pair)  # il stergem din lista cart-ului
                self.locks[pair[0]].release()  # iesim din zona thread-safe
                return True
        return False

    def place_order(self, cart_id):
        """
        Return a list with all the products in the cart.

        :type cart_id: Int
        :param cart_id: id cart
        """
        return self.carts[cart_id]  # returnam lista cart-ului de la cheia cart_id

    # metoda pentru a verifica daca lista alocata unui producator este plina
    def _producer_not_full(self, prod_id):
        return len(self.stock[prod_id]) < self.queue_size_per_prod
