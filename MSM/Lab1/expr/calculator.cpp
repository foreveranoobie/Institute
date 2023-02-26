#include <iostream>
#include <string>
#include <bits/stdc++.h>
#include "../util/mathutil.cpp"

using namespace std;

class Calculator{
    public:
        void calculateExpression(string* expression){
            float* numbers;
            bool isSpace = false;
            int count = 0;
            float* sum;
            string toInsert;
            char operation;
            int beginIndex;
            int operationIndex;
            for(int index = 0; index < expression->length() && expression->size() != 1 && stringContainsOperation(*expression); index++){
                while(!isOperation(expression->at(index))){
                    index++;
                }
                operationIndex = index;
                operation = expression->at(index);
                numbers = getNumbers(expression, index-2);
                sum = new float(calculateOperation(numbers, operation));
                //sum = new float(round(numbers[0] + numbers[1]));
                beginIndex = index;
                while(beginIndex > -1 && count < 3){
                    if(expression->at(beginIndex--) == ' '){
                        count++;
                    }
                }
                if(beginIndex == -1){
                    beginIndex = 0;
                } else if(beginIndex >= 0){
                    beginIndex += 2;
                }
                toInsert = to_string(*sum);
                toInsert.erase(toInsert.length() - 4, 4);
                expression->erase(beginIndex, index - beginIndex);
                expression->replace(beginIndex, 1, toInsert);
                index -= (index - beginIndex);
                count = 0;
            }
            delete sum;
            delete numbers;
        }
    
    private:
        float calculateOperation(float* numbers, char operation){
            float result = 0;
            switch(operation){
                case '+':
                    result = numbers[1] + numbers[0];
                    break;
                case '-':
                    result = numbers[1] - numbers[0];
                    break;
                case '*':
                    result = numbers[1] * numbers[0];
                    break;
                case '/':
                    result = numbers[1] / numbers[0];
                    break;
                default:
                    break;
            }
            return result;
        }

        bool stringContainsOperation(string exp){
            return exp.find(" +") != -1 || exp.find(" -") != -1 || exp.find(" *") != -1 || exp.find(" /") != -1;
        }
    
        float* getNumbers(string* expression, int index){
            string* number = new string;
            float* returnNumber = new float[2];
            while(index > -1 && expression->at(index) != ' '){
                number->insert(0, 1, expression->at(index--));
                if(index < 0){
                    break;
                }
            }
            returnNumber[0] = stof(*number);
            index = index - 1;
            number->erase(0, number->length());
            while(index > -1 && expression->at(index) != ' '){
                number->insert(0, 1, expression->at(index));
                index--;
            }
            returnNumber[1] = stof(*number);
            delete number;
            return returnNumber;
        }

        bool isOperation(char symbol){
            return symbol == '*' || symbol == '/' || symbol == '+' || symbol == '-';
        }
};