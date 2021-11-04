package clazz.finder.matcher.view

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class StringViewTest {

    @Test
    fun `length should be same as in input string`() {
        val string = "abcd"
        val stringView =  StringView(string)
        assertThat(stringView.length).isEqualTo(string.length)
    }
    @Test
    fun `get should return char same as in input string`() {
        val string = "ab"
        val stringView =  StringView(string)
        assertThat(stringView[0]).isEqualTo(stringView[0])
        assertThat(stringView[1]).isEqualTo(stringView[1])
    }

    @Test
    fun `get should throw exception if index not in range`() {
        val string = "12345"
        val stringView =  StringView(string)
        assertThatThrownBy { stringView[6] }
            .isExactlyInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Index 6 not in range [0, 5)")
    }

    @Test
    fun `is empty should return true on empty view and false on non empty`() {
        val empty = StringView("")
        val nonEmpty = StringView("xasx")
        assertThat(empty.isEmpty()).isTrue
        assertThat(nonEmpty.isEmpty()).isFalse
    }

    @Test
    fun `subView should be same as creating from substring`() {
        val string = "12345"
        val subview =  StringView(string).subview(3, 5)
        val substring =  StringView(string).subview(3, 5)
        assertThat(subview[0]).isEqualTo(substring[0])
        assertThat(subview[1]).isEqualTo(substring[1])
        assertThat(subview.length).isEqualTo(substring.length).isEqualTo(2)
    }

    @Test
    fun `subView should throw exception on incorrect indices`() {
        val string = "12345"
        val subview =  StringView(string)
        assertThatThrownBy {subview.subview(6, 10)}
            .isExactlyInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Index 6 not in range [0, 5)")
    }

    @Test
    fun `split by condition chunks should be on all chars that match condition`() {
        val chunks =  StringView("abcdFooBarBaz").splitByCondition { it.isUpperCase() }.map {it.toString()}
        assertThat(chunks).isEqualTo(listOf("Baz", "Bar", "Foo", "abcd"))
    }

    @Test
    fun `find should return first entry with sensitive case`() {
        val stringView =  StringView("FooBarBazBar")
        val pattern = StringView("Bar")
        assertThat(stringView.find(pattern, caseInsensitive = false, from = 2)).isEqualTo(3)
    }


    @Test
    fun `find should return first entry with insensitive case`() {
        val stringView =  StringView("FooBarBazBar")
        val pattern = StringView("Bar")
        assertThat(stringView.find(pattern, caseInsensitive = true, from = 2)).isEqualTo(3)
    }

    @Test
    fun `find should return -1 if entry non exists`() {
        val stringView =  StringView("FooBarBazBar")
        val pattern = StringView("Car")
        assertThat(stringView.find(pattern, caseInsensitive = false, from = 2)).isEqualTo(-1)
        assertThat(stringView.find(pattern, caseInsensitive = true, from = 0)).isEqualTo(-1)
    }
}