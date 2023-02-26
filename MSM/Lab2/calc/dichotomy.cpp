#include "element.cpp"
#include "../expr/calculator.cpp"
#include <vector>
#include <string>

using namespace::std;

class Dichotomy{
    private:
        float epsilon = 0.2;
        int N = 8;
        Calculator* calculator = new Calculator;
    public:
        ~Dichotomy(){
            delete calculator;
        }

        vector<Element>* calculate(string* expression, float leftEdge, float rightEdge){
            vector<Element>* result = new vector<Element>();
            float x1;
            float x2;
            float f1;
            float f2;
            float a = leftEdge;
            float b = rightEdge;
            float dividedEpsilon = epsilon / 2;
            for(int i = 1; i < N/2; i++){
                x1 = ((a + b) / 2) - dividedEpsilon;
                x2 = ((a + b) / 2) + dividedEpsilon;
                string* replacedExpression = new string(replaceVariablesWithValue(*expression, x1));
                calculator->calculateExpression(replacedExpression);
                f1 = stof(*replacedExpression);
                replacedExpression = new string(replaceVariablesWithValue(*expression, x2));
                calculator->calculateExpression(replacedExpression);
                f2 = stof(*replacedExpression);
                if(f1 > f2){
                    a = x1;
                } else {
                    b = x2;
                }
                Element element = Element(i, x1, x2, f1, f2, a, b);
                result->push_back(element);
                delete replacedExpression;
            }
            return result;
        }

        void setN(int N){
            this->N = N;
        }

        int getN(){
            return N;
        }

        void setEpsilon(float epsilon){
            this->epsilon = epsilon;
        }

        float getEpsilon(){
            return epsilon;
        }
};