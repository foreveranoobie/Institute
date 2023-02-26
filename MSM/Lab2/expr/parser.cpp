#include <iostream>
#include <vector>
#include <regex>

using namespace std;

class Parser{
    private:
        vector<string>* operatorsStack;
    public:
        Parser(){
            operatorsStack = new vector<string>;
        }

        ~Parser(){
            delete operatorsStack;
        }

        vector<string>* parse(string expression){
            vector<string>* result = new vector<string>();
            for(int index = 0; index < expression.length(); index++){
                if(isNumber(expression[index])){
                    string* number = new string;
                    while (index < expression.length() && isNumber(expression[index])) {
                        number->append(1, expression[index++]);
                    }   
                    result->push_back(*number);
                    delete number;
                    if(index == expression.length())
                        break;
                }
                string* pushSymbol = new string;
                if(expression[index] == '*' || expression[index] == '/'){
                    while(operatorsStack->size() != 0 && (operatorsStack->back() == "*" || operatorsStack->back() == "/")){
                        result->push_back(operatorsStack->back());
                        operatorsStack->pop_back();
                    }
                    pushSymbol->append(1, expression[index]);
                    operatorsStack->push_back(*pushSymbol);
                }
                else if(expression[index] == '+' || expression[index] == '-'){
                    while(operatorsStack->size() != 0 && (operatorsStack->back() == "+" || operatorsStack->back() == "-")){
                        result->push_back(operatorsStack->back());
                        operatorsStack->pop_back();
                    }
                    pushSymbol->append(1, expression[index]);
                    operatorsStack->push_back(*pushSymbol);
                }
                else if(expression[index] == '(' || expression[index] == ')'){
                    if(expression[index] == '('){
                        pushSymbol->append(1, expression[index]);
                        operatorsStack->push_back(*pushSymbol);
                    } else {
                        while (!operatorsStack->size() != 0 && operatorsStack->back() != "(") {
                            result->push_back(operatorsStack->back());
                            operatorsStack->pop_back();
                        }
                        if (!operatorsStack->size() != 0 && operatorsStack->back() == "(") {
                            operatorsStack->pop_back();
                        }
                    }
                }
                delete pushSymbol;
            }
            while(operatorsStack->size() != 0){
                result->push_back(operatorsStack->back());
                operatorsStack->pop_back();
            }
            return result;
        }

        bool isNumber(char symbol){
            return symbol - '0' >= 0 && symbol - '0' <= 9;
        }
};