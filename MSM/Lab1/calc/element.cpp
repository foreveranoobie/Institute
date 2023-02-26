class Element{
    private:
        int iteration;
        float x1;
        float x2;
        float f1;
        float f2;
        float a;
        float b;
    
    public:
        Element(){}

        Element(int iteration, float x1, float x2, float f1, float f2, float a, float b){
            this->iteration = iteration;
            this->x1 = x1;
            this->x2 = x2;
            this->f1 = f1;
            this->f2 = f2;
            this->a = a;
            this->b = b;
        }

        int getIteration(){
            return iteration;
        }

        float getX1(){
            return x1;
        }

        float getX2(){
            return x2;
        }

        float getf1(){
            return f1;
        }

        float getf2(){
            return f2;
        }

        float getA(){
            return a;
        }

        float getB(){
            return b;
        }
};