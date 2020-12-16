import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle16 {
    public static void main(String[] args) throws IOException {
        List<String> input = CommonUtils.readInputToList();
        List<String> fieldsInput = input.subList(0, input.indexOf(""));
        List<Integer> yourTicket = Arrays.stream(input.get(input.indexOf("") + 2).split(",")).map(Integer::parseInt)
                .collect(Collectors.toList());
        List<List<Integer>> nearbyTickets = input.subList(input.lastIndexOf("") + 2, input.size()).stream()
                .map(s -> Arrays.stream(s.split(",")).map(Integer::parseInt).collect(Collectors.toList()))
                .collect(Collectors.toList());

        CommonUtils.printPartHeader(1);

        TicketScanner ticketScanner = new TicketScanner(fieldsInput);
        int errorRate = 0;
        for (List<Integer> ticket : nearbyTickets) {
            errorRate += ticketScanner.errorRate(ticket);
        }
        System.out.printf("Ticket scanning error rate: %d%n%n", errorRate);
    }
}
