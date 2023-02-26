#include "calc\scan.cpp"
#include "expr\formatter.cpp"
#include "expr\parser.cpp"
#define _CRTDBG_MAP_ALLOC
#include <stdlib.h>
#include <crtdbg.h>

using namespace::std;

int main(){
    string* expression = new string();
    getline(cin, *expression);
    cout << *expression << endl;
    //cout << replaceVariablesWithValue(*expression, 1.52) << endl;
    Scan* scanMethod = new Scan();
    vector<Element>* calculation = scanMethod->scanExpression(expression, -1, 1);
    vector<Element>::iterator resultIterator = calculation->begin();
    while(resultIterator != calculation->end()){
        Element currentElement = *resultIterator;
        printf("alpha = %f; betta = %f; xi = %f; a = %f; b = %f | fi = %f => x* = %f, f* = %f;\n", 
            currentElement.getAlpha(), currentElement.getBetta(), currentElement.getXi(), currentElement.getA(), currentElement.getB(), 
            currentElement.getfi(), currentElement.getX(), currentElement.getf());
        resultIterator++;
    }
    delete expression;
    delete scanMethod;
    delete calculation;
    system("pause");
    return 0;
}