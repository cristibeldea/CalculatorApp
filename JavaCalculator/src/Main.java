import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {

        Diferentiere expresie[] = new Diferentiere[100];

        for(int i=0; i<expresie.length ;i++)
            expresie[i]= new Diferentiere();


        int n = citire(expresie); // n este numarul de caractere din expresie

        n = determinareNumere(expresie, n); //n se schimba dupa transformarea din cifre in numere deoarece cateva sloturi o sa dispara




       // for(int i=0;i<n;i++)
          //  System.out.println(expresie[i].numar + " " + "TIP:"+ expresie[i].type + " INDEX:" +expresie[i].indexParanteza);

        double rezultat = calcul(expresie, n);
        System.out.println("\nREZULTAT: "+ rezultat);

		/*for(int i=0;i<n;i++)
			System.out.println(expresie[i].numar + " " + "TIP:"+ expresie[i].type + " INDEX:" +expresie[i].indexParanteza); */
    }



    public static int citire(Diferentiere[] expresie) throws Exception {

        FileReader fileReader = new FileReader("src//evaluare.in");
        int i=0;
        while ((expresie[i].aux = fileReader.read()) != -1) {
            i++;
        }
        fileReader.close();

        clasificare(expresie, i);

        return i;
    }



    public static void clasificare(Diferentiere[] expresie, int n) throws Exception {
        int i;
        int k=1;

        for(i=0;i<n;i++)
        {
            if('0'<=expresie[i].aux && expresie[i].aux<='9')
            {
                expresie[i].type="numar";
                expresie[i].numar = expresie[i].aux-'0';
            }

            if(expresie[i].aux == '-' || expresie[i].aux == '+' || expresie[i].aux == '/' || expresie[i].aux == '*' || expresie[i].aux == '^')
            {
                expresie[i].type="operator";
                expresie[i].simbol = (char)expresie[i].aux;
            }

            if(expresie[i].aux == '(' || expresie[i].aux == ')')
            {
                expresie[i].simbol = (char)expresie[i].aux;


                if(expresie[i].simbol == '(')
                {
                    expresie[i].indexParanteza = k;
                    expresie[i].type = "parantezaOpened";
                    k++;
                }


                if(expresie[i].simbol == ')')
                {
                    k--;
                    expresie[i].indexParanteza = k;
                    expresie[i].type = "parantezaClosed";
                }
            }

            if(expresie[i].aux == ',')
            {
                expresie[i].type = "virgula";
                expresie[i].simbol = (char)expresie[i].aux;
            }
        }
    }



    public static int determinareNumere(Diferentiere[] expresie, int n) {

        int i;
        double number=0;
        double number2=0;
        int counterCifre, counterDupaVirgula;
        for(i=0;i<n;i++)
        {
            counterCifre=0;
            counterDupaVirgula=0;
            number=0;
            number2=0;

            if(expresie[i].type == "numar")
            {
                while(expresie[i]!=null && expresie[i].type == "numar")
                {
                    counterCifre++;
                    number = number * 10 + expresie[i].numar;
                    i++;
                }

                if(expresie[i]!=null && expresie[i].type == "virgula")
                {
                    i++;
                    while(expresie[i]!=null && expresie[i].type == "numar")
                    {
                        counterDupaVirgula++;
                        number2 = number2 * 10 + expresie[i].numar;
                        i++;
                    }

                    number2 = number2 / Math.pow(10, counterDupaVirgula);
                    counterDupaVirgula++; //includem virgula
                }

                counterCifre = counterCifre + counterDupaVirgula;
                number = number + number2;
                i = i-counterCifre +1;

                expresie[i-1].numar = number;
                n = realocare(expresie,i,counterCifre,n);
            }
        }

        return n;
    }



    public static int realocare(Diferentiere[] expresie, int pozitie, int nrCifre, int n) {

        for(int i=pozitie; i<n-(nrCifre-1);i++)
            expresie[i] = expresie[i+(nrCifre-1)];
        expresie[n-(nrCifre-1)] = null;

        n = n - (nrCifre-1);
        return n;

    }



    public static double calcul(Diferentiere[] expresie, int n) {

        int i, nrParanteza=0;
        int saveIndex;

        Diferentiere expresieParanteza[] = new Diferentiere[100];
        for(i=0;i<expresieParanteza.length;i++)
            expresieParanteza[i] = new Diferentiere();

        for(i=0;i<n;i++) {
            nrParanteza=0;
            //System.out.println(i);
            if(expresie[i]!=null && expresie[i].type == "parantezaOpened")
            {
                saveIndex = expresie[i].indexParanteza;
                i++;
                if(expresie[i].simbol == '-')
                {
                    expresie[i-1].numar = (-1)* expresie[i+1].numar;
                    expresie[i-1].type = "numar";
                    expresie[i-1].simbol = '\0';

                    n = realocareOperatori(expresie,i,n);
                    i--;
                }

                else
                {
                    while(expresie[i].indexParanteza != saveIndex) {
                        expresieParanteza[nrParanteza]=expresie[i];
                        i++;
                        nrParanteza++;

                        //System.out.println(nrParanteza + "@@@ " + saveIndex + " @@@ " + expresie[i].simbol);
                    }

                   // for(int h=0;h<nrParanteza;h++)
                       // System.out.println(expresieParanteza[h].numar + " " + "TIP:"+ expresieParanteza[h].type +" SIMBOL:" +expresieParanteza[h].simbol + " INDEX:" +expresieParanteza[h].indexParanteza);

                    i=i-nrParanteza;
                    expresie[i-1].numar = calcul(expresieParanteza, nrParanteza);
                    //System.out.println("VERIFICARE: "+expresie[i-1].numar);

                    expresie[i-1].type = "numar";
                    expresie[i-1].simbol = '\0';
                    expresie[i-1].indexParanteza = 0;

                   // for(int h=0;h<n;h++)
                       // System.out.println(expresie[h].numar + " " + "TIP:"+ expresie[h].type + " INDEX:" +expresie[h].indexParanteza);

                    n = realocareParanteze(expresie,i,nrParanteza+1,n);
                }
            }
        }

        for(i=0;i<n;i++)
        {
            if(expresie[i]!=null && expresie[i].simbol == '^')
            {
                expresie[i-1].numar = Math.pow(expresie[i-1].numar, expresie[i+1].numar);
                expresie[i-1].type = "numar";
                expresie[i-1].simbol = '\0';

                n = realocareOperatori(expresie,i,n);
                i--;
            }
        }

        for(i=0;i<n;i++)
        {
            if(expresie[i]!=null && expresie[i].simbol == '*')
            {
                expresie[i-1].numar = expresie[i-1].numar * expresie[i+1].numar;
                expresie[i-1].type = "numar";
                expresie[i-1].simbol = '\0';
                n = realocareOperatori(expresie,i,n);
                i--;
            }


            if(expresie[i]!=null && expresie[i].simbol == '/')
            {
                expresie[i-1].numar = expresie[i-1].numar / expresie[i+1].numar;
                expresie[i-1].type = "numar";
                expresie[i-1].simbol = '\0';

                n = realocareOperatori(expresie,i,n);
                i--;
            }
        }

        for(i=0;i<n;i++)
        {
            if(expresie[i]!=null && expresie[i].simbol == '+')
            {
                expresie[i-1].numar = expresie[i-1].numar + expresie[i+1].numar;
                expresie[i-1].type = "numar";
                expresie[i-1].simbol = '\0';

                n = realocareOperatori(expresie,i,n);
                i--;
            }


            if(expresie[i]!=null && expresie[i].simbol == '-')
            {
                expresie[i-1].numar = expresie[i-1].numar - expresie[i+1].numar;
                expresie[i-1].type = "numar";
                expresie[i-1].simbol = '\0';

                n = realocareOperatori(expresie,i,n);
                i--;
            }
        }

        return expresie[0].numar;
    }

    public static int realocareParanteze(Diferentiere[] expresie, int pozitie, int nrSpatii, int n) {
        int i;
        for(i=pozitie;i<n-nrSpatii;i++)
            expresie[i] = expresie[i+nrSpatii];
        n = n - nrSpatii;
        expresie[n] = null;

        return n;
    }

    public static int realocareOperatori(Diferentiere[] expresie, int pozitie, int n) {
        for(int j=pozitie;j<n;j++)
            expresie[j]=expresie[j+2];
        n=n-2;
        expresie[n]=null;

        return n;
    }
}