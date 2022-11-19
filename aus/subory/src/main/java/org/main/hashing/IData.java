package org.main.hashing;

import java.util.BitSet;

public abstract class IData<T> implements IRecord<T> {
    /**
     * pole bytov si mozem previest na co potrebujem cislo alebo
     * podla neho traverzovat v strome
     *
     * @return pole bytov
     */
    public abstract BitSet getHash();

    /**
     * porovna dva prvky
     *
     * nemozem porovnavat vysledok getHash pretoze dva rozne prvky mozu mat rovnaky
     * vysledok hash funkcie
     */
    public abstract boolean myEqual(T data);

    /**
     * ? nieco ako kopirovaci konstruktor, nieco co mi tu triedu namnozi
     * @return
     */
    public abstract T createClass();

    /**
     * FOR TESTING, compare toByteArray().length with this.getSize()
     */
    public void testSize() {
        if (this.toByteArray().length != this.getSize()) {
            System.out.println("ERROR: wrong size");
            System.out.println("getSize: " + this.getSize());
            System.out.println("toByteArray: " +this.toByteArray().length);
        }
    }
}
