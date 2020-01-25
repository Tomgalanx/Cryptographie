import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Schnorr {

    private Random random;
    private BigInteger q,p,g,Ks,Kp,a,c;



    public Schnorr(){

        random=new SecureRandom();
        q=BigInteger.ZERO;
        p=BigInteger.ZERO;
        g=BigInteger.ZERO;
        Ks=BigInteger.ZERO;
        Kp=BigInteger.ZERO;
        c=BigInteger.ZERO;

    }


    public void KeyGen(int n,int o){

        //System.out.println("Génération des clefs :");

        BigInteger two = BigInteger.TWO;
        int estPremier = 100;


        // 1 Choisir q d'au moins n bits
        q = new BigInteger(n, estPremier, random);

        // 2 Choisir un entier k aleatoire d'au moins o bits
        BigInteger k =new BigInteger(o,random);


        // 3 Si p = 1 + kq n’est pas premier, revenir a l’́etape 2.
        do {

            // Calcule de p
            p=k.multiply(q);
            p=p.add(BigInteger.ONE);

            // verifie si p est premier
            if (p.isProbablePrime(estPremier)) break;

            // Si il n'est pas premier, on ajoute 1 et on recommence
            k = k.add(BigInteger.ONE);
        } while (true);

        //System.out.println("P est premier :"+p.isProbablePrime(100));




        BigInteger a;
        while (true) {

            a = (two.add(new BigInteger(p.bitLength()-1, random)));
            BigInteger ga = p.divide(q);
            g = a.modPow(ga, p);
            if (g.compareTo(BigInteger.ONE) != 0)
                break;

        }

        //System.out.println("Fin des modulos");


        Key();

    }

    public void Key(){

        // Calcul du couple Kp et Ks


        // Ks = x où x est un entier choisi entre 2 et q-2
        Ks = (BigInteger.TWO.add(new BigInteger(q.bitLength()-1, random))).mod(q);

        // Kp = g^x (mod p)
        Kp = g.modPow(Ks,p);
    }

    public void Sign(String message) throws NoSuchAlgorithmException {

        //System.out.println("Méthode Sign");
        // Nombre entre 2 et q-2
        BigInteger r =(BigInteger.TWO.add(new BigInteger(q.bitLength()-1, random)));
        BigInteger R = g.modPow(r,p);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String tmp = R+message;
        byte[] hash = digest.digest(tmp.getBytes());

        c =new BigInteger(hash);
        c =c.mod(q);

        a = c.multiply(Ks);
        a = r.subtract(a);
        a = a.mod(q);


        //System.out.println("C = "+ c.bitLength());
        //System.out.println("A = "+a.bitLength());
    }


    public void Verify(String message) throws NoSuchAlgorithmException {


        BigInteger X1 = g.modPow(a,p);
        BigInteger X2 = Kp.modPow(c,p);

        BigInteger R = X1.multiply(X2).mod(p);


        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String tmp = R+message;
        byte[] hash = digest.digest(tmp.getBytes());

        BigInteger H =new BigInteger(hash).mod(q);


        if(H.equals(c)){
            System.out.println("Signature ok");
            System.out.println(c.add(a).bitLength());
            //System.out.println("Public Key :"+Kp.bitLength()+" "+Kp);
            //System.out.println("Private Key :"+Ks.bitLength()+" "+Ks);
        }
        else{
            System.out.println("Pas la bonne signature ");
        }


    }


}