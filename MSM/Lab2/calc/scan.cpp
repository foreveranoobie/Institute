#include "element.cpp"
#include "../expr/calculator.cpp"
#include <vector>
#include <string>
#include <ctime>

class Scan{
    private:
        int N = 10;
        Calculator* calculator = new Calculator();
    public:
        ~Scan(){
            delete calculator;
        }

        vector<Element>* scanExpression(string* expression, float leftEdge, float rightEdge){
            srand (time(NULL));
            float alphaInitial;
            float bettaInitial;
            float xi;
            float x;
            float fi;
            float f;
            float a = leftEdge;
            float b = rightEdge;
            int* doubleN = new int(N * 2);
            //as alpha
            x = generateOutOfRangeNumber(a, b);
            //as beta
            f = 1000;
            vector<Element>* iterations = new vector<Element>();
            for(int i = 1; i <= N; i++){
                xi = a + ((b - a) / *doubleN) * (2 * i - 1);
                string* replacedExpression = new string(replaceVariablesWithValue(*expression, xi));
                calculator->calculateExpression(replacedExpression);
                fi = stof(*replacedExpression);
                if(fi < f){
                    alphaInitial = x;
                    bettaInitial = f;
                    x = xi;
                    f = fi;
                }
                Element* currentIteration = new Element(i, xi, x, fi, f, a, b, alphaInitial, bettaInitial);
                iterations->push_back(*currentIteration);
                delete replacedExpression;
            }
            delete doubleN;
            return iterations;
        }

        int generateOutOfRangeNumber(float leftEdge, float rightEdge){
            //Random value which decides whether alpha's value should be after rightEdge or before leftEdge
            //if alpha should be less than leftEdge
            if(rand() % 2){
                if(leftEdge < 0){
                    return -1 * (rand() % 1000) + (leftEdge * -1) + 1;
                } else {
                    //if generated value should ne negative
                    if(rand() % 2){
                        return -1 * (rand() % 1000) + 1;
                    } 
                    //if generated value should ne positive
                    else {
                        return (rand() % 1000) + 1;
                    }
                }
            //if alpha should be more than rightEdge
            } else {
                return rand() % 1000 + rightEdge + 1;
            }
        }
};