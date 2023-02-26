class Element{
    private:
        int iteration;
        float initialAlpha;
        float initialBetta;
        float xi;
        float x;
        float fi;
        float f;
        float a;
        float b;
    
    public:
        Element(){}

        Element(int iteration, float xi, float x, float fi, float f, float a, float b, float initialAlpha, float initialBetta){
            this->iteration = iteration;
            this->xi = xi;
            this->x = x;
            this->fi = fi;
            this->f = f;
            this->a = a;
            this->b = b;
            this->initialAlpha = initialAlpha;
            this->initialBetta = initialBetta;
        }

        int getIteration(){
            return iteration;
        }

        float getXi(){
            return xi;
        }

        float getX(){
            return x;
        }

        float getfi(){
            return f;
        }

        float getf(){
            return f;
        }

        float getA(){
            return a;
        }

        float getB(){
            return b;
        }

        float getAlpha(){
            return initialAlpha;
        }

        float getBetta(){
            return initialBetta;
        }
};