#include <iostream>
#include <string>
#include <regex>

using namespace::std;

class Validator{
    public:
        bool validate(string expression){
            return validateBrackets(expression) && checkExpressions(expression);
        }
    
    private:
        bool validateBrackets(string expression){
            int leftBracketsCount = 0;
            int rightBracketsCount = 0;
            for (int expressionIndex = 0; expressionIndex < expression.length(); expressionIndex++) {
                if(expression[expressionIndex] == '(')
                    leftBracketsCount++;
                else if(expression[expressionIndex] == ')')
                    rightBracketsCount++;
            }
            return leftBracketsCount == rightBracketsCount;
        }

        bool checkExpressions(string expression){
            return regex_match(expression, regex("^[\\(\\dx][\\dx\\(\\)+\\-*/(\\^\\dx)]+[\\)\\dx]$"));
        }
};