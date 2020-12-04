all: homeworkFT homeworkFFT inputGenerator compareOutputs

compareOutputs: ./src/compareOutputs.c
	gcc -o compareOutputs ./src/compareOutputs.c -O3 -lm -Wall

inputGenerator: ./src/inputGenerator.c
	gcc -o inputGenerator ./src/inputGenerator.c -O3 -lm -Wall

homeworkFT: ./src/homeworkFT.c
	gcc -o homeworkFT ./src/homeworkFT.c -O3 -lpthread -lm -Wall

homeworkFFT: ./src/homeworkFFT.c
	gcc -o homeworkFFT ./src/homeworkFFT.c -O3 -lpthread -lm -Wall

clean:
	rm homeworkFFT homeworkFT inputGenerator compareOutputs