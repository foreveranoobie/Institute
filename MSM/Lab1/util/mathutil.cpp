#include <string>

using namespace std;

 float round(float var)
{
    float value = (int)(var * 100 + .5);
    return (float)value / 100;
}

string replaceVariablesWithValue(string expression, float value){
    int index = expression.find("x");
    string* replaceValue = new string(to_string(value));
    replaceValue->erase(replaceValue->length() - 4, 4);
    while(index != -1){
        expression.erase(index, 1);
        expression.insert(index, *replaceValue);
        index = expression.find("x");
    }
    delete replaceValue;
    return expression;
}