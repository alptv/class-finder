package clazz.finder.matcher

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ClassMatcherTest {

    @Test
    fun `empty pattern should match all`() {
        val matcher = ClassMatcher("")
        assertThat(matcher.match("FooBazBar")).isTrue
        assertThat(matcher.match("ABCD")).isTrue
        assertThat(matcher.match("X")).isTrue
    }


    @Test
    fun `should match by upper case in right order`() {
        var matcher = ClassMatcher("FBB")
        assertThat(matcher.match("FooBazBar")).isTrue

        matcher = ClassMatcher("ABCD")
        assertThat(matcher.match("ABCD")).isTrue

        matcher = ClassMatcher("X")
        assertThat(matcher.match("X")).isTrue
    }

    @Test
    fun `should not match find by upper case in wrong order`() {
        var matcher = ClassMatcher("BBF")
        assertThat(matcher.match("FooBazBar")).isFalse

        matcher = ClassMatcher("ABCD")
        assertThat(matcher.match("DBAC")).isFalse

    }

    @Test
    fun `should match by upper case in right order with lower case letters`() {
        var matcher = ClassMatcher("FooBazBar")
        assertThat(matcher.match("FooBazBar")).isTrue

        matcher = ClassMatcher("ABuCaDoo")
        assertThat(matcher.match("AaaBumCarDoom")).isTrue

        matcher = ClassMatcher("xxXxxxx")
        assertThat(matcher.match("xxXxxxxxxxxxx")).isTrue
    }

    @Test
    fun `should not match by upper case in wrong order with lower case letters`() {
        var matcher = ClassMatcher("FooBazBar")
        assertThat(matcher.match("BazBarFoo")).isFalse

        matcher = ClassMatcher("ABuCaDoo")
        assertThat(matcher.match("BumAaaCarDoom")).isFalse

    }

    @Test
    fun `should match by upper not all upper case letters`() {
        var matcher = ClassMatcher("FooBazBar")
        assertThat(matcher.match("FooCarBazDoomBar")).isTrue

        matcher = ClassMatcher("ABuCaDoo")
        assertThat(matcher.match("AaaXxBumYyCarDoomXxYyZ")).isTrue

    }

    @Test
    fun `should match with insensitive case`() {
        var matcher = ClassMatcher("fbb")
        assertThat(matcher.match("FooBarBaz")).isTrue

        matcher = ClassMatcher("fbb")
        assertThat(matcher.match("FooDomBarCarBaz")).isTrue

        matcher = ClassMatcher("foodob")
        assertThat(matcher.match("FooDomBarCarBaz")).isTrue

        matcher = ClassMatcher("abucadooz")
        assertThat(matcher.match("AaaXxBumYyCarDoomXxYyZ")).isTrue

    }

    @Test
    fun `should not match with insensitive case`() {
        var matcher = ClassMatcher("fbab")
        assertThat(matcher.match("FooBooCarBaz")).isFalse

        matcher = ClassMatcher("fbbazb")
        assertThat(matcher.match("FooBarBaz")).isFalse


        matcher = ClassMatcher("aaaxbuadooz")
        assertThat(matcher.match("AaaXxBumYyCarDoomXxYyZ")).isFalse
    }

    @Test
    fun `should match if pattern has wildcard and sensitive case`() {
        var matcher = ClassMatcher("F*B*B*")
        assertThat(matcher.match("FooBarBaz")).isTrue


        matcher = ClassMatcher("F*oB*r*")
        assertThat(matcher.match("FooBarBaz")).isTrue

        matcher = ClassMatcher("a*cDo*B*rB")
        assertThat(matcher.match("abcdFooDomBarCarBaz")).isTrue

        matcher = ClassMatcher("FooDo*")
        assertThat(matcher.match("FooDomBarCarBaz")).isTrue

        matcher = ClassMatcher("aA*Bu*rZ")
        assertThat(matcher.match("aaAaaXxBumYyCarDoomXxYyZ")).isTrue
    }

    @Test
    fun `should match if pattern has wildcard and insensitive case`() {
        var matcher = ClassMatcher("f*b*b*")
        assertThat(matcher.match("FooBarBaz")).isTrue


        matcher = ClassMatcher("fb*")
        assertThat(matcher.match("FooBarBaz")).isTrue

        matcher = ClassMatcher("f**b*b**")
        assertThat(matcher.match("FooDomBarCarBaz")).isTrue

        matcher = ClassMatcher("foodo*b*z")
        assertThat(matcher.match("FooDomBarCarBaz")).isTrue

        matcher = ClassMatcher("a*buc*rdo*x*")
        assertThat(matcher.match("AaaXxBumYyCarDoomXxYyZ")).isTrue
    }

    @Test
    fun `should not find if wildcard not match`() {
        var matcher = ClassMatcher("*fbb")
        assertThat(matcher.match("FooBarBaz")).isFalse

        matcher = ClassMatcher("B*rBaz")
        assertThat(matcher.match("BrBaz")).isFalse

        matcher = ClassMatcher("f******b****b*****")
        assertThat(matcher.match("FooDomBarCarBaz")).isFalse

        matcher = ClassMatcher("fo*odo*b*z")
        assertThat(matcher.match("FooDomBarCarBaz")).isFalse

        matcher = ClassMatcher("*A*bu**c*rdo*x*")
        assertThat(matcher.match("aAaaXxBumYyCarDoomXxYyZ")).isFalse
    }

    @Test
    fun `should match only if classname end on last word`() {
        var matcher = ClassMatcher("FBar ")
        assertThat(matcher.match("FooBar")).isTrue

        matcher = ClassMatcher("FBar ")
        assertThat(matcher.match("FooBarBaz")).isFalse

        matcher = ClassMatcher("FBa ")
        assertThat(matcher.match("FooBar")).isFalse

        matcher = ClassMatcher("FBa ")
        assertThat(matcher.match("FooBaBaz")).isFalse

        matcher = ClassMatcher("SME*d ")
        assertThat(matcher.match("StartMiddleEnd")).isTrue

        matcher = ClassMatcher("smend ")
        assertThat(matcher.match("SMEnd")).isTrue

        matcher = ClassMatcher("smendddd ")
        assertThat(matcher.match("SMEnd")).isFalse
    }
}