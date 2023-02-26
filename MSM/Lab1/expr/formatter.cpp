#include <iostream>
#include <string>

using namespace::std;

class Formatter{
    public:
        string formatExpression(string expression){
            for (int expressionIndex = 1; expressionIndex < expression.length(); expressionIndex++) {
                if(expression[expressionIndex] == '^'){
                    int power = expression[expressionIndex + 1] - '0';
                    char* copyPower = new char();
                    if(expression[expressionIndex - 1] == ')'){
                        int indexStart = getPowerIndexStart(expression, expressionIndex - 2);
                        int length = expressionIndex - indexStart;
                        expression.copy(copyPower, length, indexStart);
                        expression.erase(expressionIndex + 1, 1);
                        int indexToCopyTo = expressionIndex;
                        for(int i = 0; i < power - 1; i++){
                            expression.replace(indexToCopyTo, 1, 1, '*');
                            expression.insert(indexToCopyTo + 1, copyPower);
                            indexToCopyTo += length + 1;
                        }
                    } else {
                        char* number = new char;
                        expression.copy(number, 1, expressionIndex - 1);
                        int indexStart = expressionIndex;
                        for(int i = 0; i < power - 1; i++){
                            expression.replace(indexStart, 1, 1, '*');
                            expression.insert(indexStart + 1, number);
                            indexStart += 2;
                        }
                        delete number;
                        cout << expression << " - " << expression.length() << endl;
                    }
                    delete copyPower;
                }
            }
            return expression;
        }

    private:
        int getPowerIndexStart(string expression, int index){
            int rightCount = 1;
            int leftCount = 0;
            while((rightCount != leftCount && rightCount > 0) && index > -1){
                if(expression[index] == ')'){
                    rightCount++;
                }
                else if(expression[index] == '('){
                    leftCount++;
                }
                index--;
            }
            return index + 1;
        }
};