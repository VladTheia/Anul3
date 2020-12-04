Vlad Theia Madalin
334CC

In implementarea temei am completat functiile oferite in schelet in vederea rezolvarii problemei multi-producer 
multi-consumer. 

Producator:
In constructorul producatorului am parsat argumentele primite.
In functia run, am facut un while loop infinit care se va oprii la incheiea rularii consumatorilor pentru ca 
producatorii sunt thread-uri daemon. In loop iteram prin lista de produse de care dispune producatorul, si 
incercam sa le publicam in marketplace. Daca lista este plina sau daca am reusit sa publicam produsul, asteptam 
o perioada de timp.

Consumator:
In constructor am parsat argumentele primite.
In run, iteram prin cart-urile primite si pentru fiecare obtinem un cart_id de la marketplace. Urmeaza sa iteram 
prin tuplurile de actiuni din fiecare cart si executam actiunea primita de 'quantity' ori pe produsul din acel tuplu.
Daca adaugam, cat timp nu gasim produsul in marketplace, asteptam o perioada si reincercam. Daca stergem produsul din 
cart, il readaugam in lista de produse din care provenea. Dupa ce terminam actiunile unui cart, plasam comanda.

Marketplace:
In constructor am definit: 
- limita de elmente per lista de care dispune un producator
- dictionarul stock, care contine listele fiecarui producator, cheile fiind prod_id
- dictionarul de lock-uri pentru fiecare producator, tot cu cheile prod_id
- lista de cart-uri, cart-urile fiind si ele liste de produse
- index-uri pentru prod_id si cart_id

Restul metodelor:
- register_producer: am creat un string care reprezinta un id ce creste la inregistrarea fiecarui producator.
- publish: am pus produsul primit in lista producatorului cat timp aceasta nu depasea limita de elemente permise.
- new_cart: am returnat un id pentru un cart nou si am adaugat o lista goala in carts
- add_to_cart: am iterat prin toate produsele din listele fiecarui producator pana am intalnit produsul dorit, 
               dupa care l-am adaugat in cart-ul primit ca argument (impreuna cu id-ul producatorului de la 
               care am luat produsul) si l-am scos din lista dorita
- remove_from_cart: am iterat prin produsele din cart pana l-am intalnit pe cel care trebuie scos, apoi l-am 
                    scos din cart si l-am adaugat inapoi in lista producatorului de la care a fost luat.
- place_order: intoarce lista cart-ului primit ca argument
- producer_not_full: intoarce true daca numarul de produse din lista producatorului nu depaseste limita