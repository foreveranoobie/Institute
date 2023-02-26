#include "calc\dichotomy.cpp"
#include "expr\formatter.cpp"
#include "expr\parser.cpp"
#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>

using namespace::std;

int main(){
    string* expression = new string();
    getline(cin, *expression);
    //Calculator* calculator = new Calculator();
    //calculator->calculateExpression(expression);
    cout << *expression << endl;
    //cout << replaceVariablesWithValue(*expression, 1.52) << endl;
    Dichotomy* dichotomy = new Dichotomy();
    vector<Element>* calculation = dichotomy->calculate(expression, 0, 2);
    vector<Element>::iterator resultIterator = calculation->begin();
    while(resultIterator != calculation->end()){
        Element currentElement = *resultIterator;
        printf("x1 = %f; x2 = %f; a = %f; b = %f; f1 = %f; f2 = %f\n", currentElement.getX1(), currentElement.getX2(),
            currentElement.getA(), currentElement.getB(), currentElement.getf1(), currentElement.getf2());
        resultIterator++;
    }
    //delete calculator;
    delete expression;
    delete dichotomy;
    delete calculation;
/*    cout << *expression << endl;
    Formatter* formatter = new Formatter();
    Parser* parser = new Parser();
    string formattedString = formatter->formatExpression(*expression);
    cout << formattedString << endl;
    vector<string>* formattedExpression = parser->parse(formattedString);
    for(vector<string>::iterator it = formattedExpression->begin(); it != formattedExpression->end(); ++it){
        cout << ' ' << *it;
    }
    delete formatter;
    delete parser;
    _CrtDumpMemoryLeaks();*/
    system("pause");
    return 0;
}