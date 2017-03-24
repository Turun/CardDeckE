from random import shuffle
from copy import copy

maxIter = 10
Nmatch = 0
Ntotal = 0
cards1 = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,
          13,14,15,16,17,18,19,20,21,22,23,24,25,
          27,28,29,30,31,32,33,34,35,36,37,38,39,
          26,40,41,42,43,44,45,46,47,48,49,50,51]

cards2 = copy(cards1)


def mixCards():
    global cards2
    global cards1
    shuffle(cards1)
    shuffle(cards2)

def draw():
    for i in range(0, len(cards1)-1):
        if cards1[i] == cards2[i]:
            return 1
    return 0
            

def start():
    global Nmatch
    global Ntotal
    global maxIter

    Nmatch = 0
    Ntotal = 0
    exitProg = False
    
    inPut = input('How many iterations?\n')
    try:
        maxIter = int(inPut)
    except:
        if inPut == "exit":
            print('Bye!')
            exitProg = True
        else:
            print('make sure to enter a number.')
            start()
            
    if not exitProg:
        Ntotal = maxIter
        print('')
        while maxIter > 0:
            maxIter -= 1
            if maxIter % 1000 == 0:
                print(Ntotal-maxIter)
            mixCards()
            Nmatch += draw()
        try:
            print('\nOf '+str(Ntotal)+' draws '+str(Ntotal-Nmatch)+' were a not hit.')
            print('This gives a probability of '+str((Ntotal-Nmatch)/Ntotal)+' for not getting a hit.')
            print('This gives a value of '+str(Ntotal/(Ntotal-Nmatch))+' for e.\n')
        except:
            Nmatch -= 1
            if Nmatch < 1:
                Nmatch = 1
            print('\nOf '+str(maxIter)+' draws '+str(Ntotal-Nmatch)+' were a not hit.')
            print('This gives a probability of '+str((Ntotal-Nmatch)/Ntotal)+' for not getting a hit.')
            print('This gives a value of '+str(Ntotal/(Ntotal-Nmatch))+' for e.\n')
        start()

start()
