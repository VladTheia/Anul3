Vlad Theia-Madalin
Grupa 334CC
Tema 2 ASC

- tema2_blas:
In implementarea variantei blas am folosit o singura functie oferita de blas:
dtrmm - pentru inmultirea intre o matrice triunghiulara si o alta matrice, 
ambele avand numere reale (double). Pentru ca functia suprascrie matricea 
non-triunghiulara, a trebuit sa creez doua copii: auxA si auxB. Intai am facut 
inmultirea dintre B si A^t (transpus) folosind parametrii oferiti de dtrmm 
pentru a folosi transpusa matricei A si pentru a o plasa in partea dreapta a 
operandului de inmultire. Astfel rezultatul este stocat in variabila B. Apoi, 
Am facut A^2 inmultind A cu copia sa (auxA) si stocand rezultatul in copie.
Dupa care am facut A^2 * B prin inmultirea lui auxA (fiind tot triunghiulara) 
cu copia lui B (auxB). In final am obtinut rezultatul adunand valorile stocate 
in B si auxB (adica B * A^t si A^2 * B).
Timpi de rulare:
N=400 - Time=0.028
N=800 - Time=0.202
N=1200 - Time=0.659

- tema2_neopt:
In implementarea variantei neoptimizate, am folosit algoritmul naiv, intuitiv 
de inmultire a matricelor, dar tinand cont de matricea triunghiulara unde era 
cazul. A fost nevoie de 2 matrice zeroizate (mat1 si mat2) unde sa stochez 
rezultatele pentru B ^ A^t si pentru A * A. In C stocam A^2 * B, apoi adunam 
cu B * A^t.
Timpi de rulare:
N=400 - Time=0.785
N=800 - Time=5.891
N=1200 - Time=19.563


- tema2_opt_m
Si in implementarea otimizata folosim cele 2 matrice mat1 si mat2 pentru 
stocarea rezultatelor, dar difera prin folosirea registrului 'suma'. Fiind 
cea mai des accesata variabila, faptul ca este un registru reduce timpul. 
De asemenea, folosim metoda cu pointeri: in loc sa accesam A si B in maniera 
obisnuita (ca pe un vector de fiecare data), salvam adresa o adresa de baza 
din memorie, pe care o incrementam adecvat in functie de loop-ul in care suntem
si accesarea de care avem nevoie. In plus, am redus numarul de loop-uri, 
calculand B * A^t si A * A in acelasi loop principal. In final am stoca in C 
suma de care eram interesati. 
Timpi de rulare:
N=400 - Time=0.349
N=800 - Time=2.774
N=1200 - Time=9.194
Asta inseamna un speed-up de aproximativ 45% fata de varianta neoptimizata.

- tema2_opt_f
Timpi de rulare:
N=400 - Time=0.194
N=800 - Time=1.518
N=1200 - Time=5.008

- tema2_opt_f_extra
Am folosit in plus flag-ul -ffast-math pentru a obtine o performanta de 9% 
pentru cazul N=1200
Timpi de rulare:
N=400 - Time=0.218
N=800 - Time=1.382
N=1200 - Time=4.516
