//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package coop.tecso.aid.printer;

public class UnicodeTranslatorStar extends UnicodeTranslator {

    /** Creates a UnicodeTranslatorStar instance of UnicodeTranslatorInt */
    public UnicodeTranslatorStar() {
    }

    public byte[] getCodeTable() {
        return new byte[] {0x1B, 0x1D, 0x74, 0x01}; // Select code page 437
    }

    public final byte[] convertString(String sConvert) {
        byte bAux[] = new byte[sConvert.length()];
        for (int i = 0; i < sConvert.length(); i++) {
            bAux[i] = transChar(sConvert.charAt(i));
        }
        return bAux;
    }

    private byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080)) {
            return (byte) sChar;
        } else {
            switch (sChar) {
                case '\u00c1': return 0x41; // A acute
                case '\u00c9': return 0x45; // E acute
                case '\u00cd': return 0x49; // I acute
                case '\u00d3': return 0x4F; // O acute
                case '\u00da': return 0x55; // U acute
                case '\u00C7': return -0x80; // 0x80 : C cedilla
                case '\u00FC': return -0x7F; // 0x81 : u dieresis
                case '\u00E9': return -0x7E; // 0x82 : e acute
                case '\u00E4': return -0x7C; // 0x84 : a dieresis
                case '\u00E5': return -0x7A; // 0x86 : a circle
                case '\u00E7': return -0x79; // 0x87 : c cedilla
                case '\u00C4': return -0x72; // 0x8E : A dieresis
                case '\u00C5': return -0x71; // 0x8F : A circle
                case '\u00F6': return -0x6C; // 0x94 : o dieresis
                case '\u00D6': return -0x67; // 0x99 : O dieresis
                case '\u00DC': return -0x66; // 0x9A : U dieresesis
                case '\u00A3': return -0x64; // 0x9C : Pound currency
                case '\u00A5': return -0x63; // 0x9D : Yen currency
                case '\u00E1': return -0x60; // 0xA0 : a acute
                case '\u00ED': return -0x5F; // 0xA1 : i acute
                case '\u00F3': return -0x5E; // 0xA2 : o acute
                case '\u00FA': return -0x5D; // 0xA3 : u acute
                case '\u00F1': return -0x5C; // 0xA4 : n tilde
                case '\u00D1': return -0x5B; // 0xA5 : N tilde
                case '\u00BF': return -0x58; // 0xA8 : open ?
                case '\u00A1': return -0x53; // 0xAD : open !
                case '\u20AC': return -0x12; // 0xEE : euro sign
                default: return 0x3F; // ? Not valid character.
            }
        }
    }
}