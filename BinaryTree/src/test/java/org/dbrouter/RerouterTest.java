package org.dbrouter;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RerouterTest {
  @Test
  public void reRouter() {
    ReRouter router = new ReRouter();

    int i = 2;
    final int MAX = 1 << 16;
    while (i <= MAX) {
      int[] res = router.reRouter(i);

      Set<Integer> sets = Sets.newHashSetWithExpectedSize(i);
      for (int n : res) {
        sets.add(n);
      }

      for (int n = 0; n < i; n++) {
        assertThat(sets.contains(n), is(true));
      }

      i <<= 1;
    }
  }
}
