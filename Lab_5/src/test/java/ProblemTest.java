

import com.jolyvert.Item;
import com.jolyvert.Problem;
import com.jolyvert.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProblemTest {

    @Test
    public void shouldGenerateCorrectNumberOfItems() {

        Problem problem = new Problem(10, 1, 1, 10);

        assertEquals(10, problem.getItems().size());
    }
    @Test
    public void shouldReturnAtLeastOneItemIfPossible() {

        Problem problem = new Problem(5, 1, 1, 10);

        Result result = problem.solve(20);

        assertFalse(result.getItemIndexes().isEmpty());
    }

    @Test
    public void shouldReturnEmptySolutionWhenCapacityTooSmall() {

        Problem problem = new Problem(5, 1, 5, 10);

        Result result = problem.solve(1);

        assertTrue(result.getItemIndexes().isEmpty());
    }

    @Test
    public void shouldReturnCorrectWeightAndValue() {

        Problem problem = new Problem(5, 1, 1, 10);

        Result result = problem.solve(20);

        assertTrue(result.getTotalWeight() <= 20);

        assertTrue(result.getTotalValue() > 0);
    }
}
