package org.main.hashing;

import java.util.BitSet;

public interface IData<T> extends IRecord<T> {
    /**
     * pole bytov si mozem previest na co potrebujem cislo alebo
     * podla neho traverzovat v strome
     *
     * @return pole bytov
     */
    BitSet getHash();

    /**
     * porovna dva prvky
     *
     * nemozem porovnavat vysledok getHash pretoze dva rozne prvky mozu mat rovnaky
     * vysledok hash funkcie
     */
    boolean myEqual(T data);

    /**
     * ? nieco ako kopirovaci konstruktor, nieco co mi tu triedu namnozi
     * @return
     */
    T createClass();
}
