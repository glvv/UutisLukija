/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.verkkokauppa;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KauppaTest {

    Pankki pankki;
    Viitegeneraattori viite;
    Varasto varasto;
    Kauppa kauppa;

    public KauppaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        pankki = mock(Pankki.class);
        viite = mock(Viitegeneraattori.class);
        varasto = mock(Varasto.class);
        kauppa = new Kauppa(varasto, pankki, viite);
        when(viite.uusi()).thenReturn(42);
        lisaaTuoteVarastoon(1, "maito", 5, 10);
        kauppa.aloitaAsiointi();
    }

    @After
    public void tearDown() {
    }

    private void lisaaTuoteVarastoon(int id, String nimi, int hinta, int saldo) {
        when(varasto.saldo(id)).thenReturn(saldo);
        when(varasto.haeTuote(id)).thenReturn(new Tuote(id, nimi, hinta));
    }

    @Test
    public void ostoksenPaatyttyaPankinMetodiaTilisiirtoKutsutaanOikeillaAsiakkaallaTilinumerollaJaSummalla() {
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 5);
    }

    @Test
    public void kunKoriinLisataanKaksiEriTuotettaJoitaVarastossaOnPankinMetodiaTilisiirtoKutsutaanOikeallaAsiakkaallaTilinumerollaJaSummalla() {
        lisaaTuoteVarastoon(2, "nakki", 12, 10);
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 17);
    }

    @Test
    public void kunOstetaanKaksiSamaaTuotettaPankinMetodiaTilisiirtoKutsutaanOikeallaAsiakkaallaTilinumerollaJaSummalla() {
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 10);
    }
    
    @Test
    public void kunLisataanOstoskoriinTuoteJotaOnVarastossaJaTuoteJotaEiOleVarastossaNiinPankinTilisiirtoMetodiaKutsutaanOikeillaParametreilla() {
        lisaaTuoteVarastoon(2, "nakki", 32, 0);
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 5);
    }
    
    @Test
    public void edellisenOstoksenHintaEiNayUudenOstoksenHinnassa() {
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        verify(pankki, times(2)).tilisiirto("pekka", 42, "12345", "33333-44455", 5);
    }
    
    @Test
    public void kauppaPyytaaUudenViitenumeronJokaMaksutapahtumalle() {
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        verify(viite, times(2)).uusi();
    }
    
    @Test
    public void kunTuotePoistetaanKoristaKutsutaanVarastonPalautaVarastoonMetodia() {
        kauppa.lisaaKoriin(1);
        kauppa.poistaKorista(1);
        verify(varasto).palautaVarastoon(any(Tuote.class));
    }
}
