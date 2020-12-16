import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle16 {
    public static void main(String[] args) throws IOException {
        List<String> input = CommonUtils.readInputToList();
        List<String> fieldsInput = input.subList(0, input.indexOf(""));
        List<Integer> myTicket = Arrays.stream(input.get(input.indexOf("") + 2).split(",")).map(Integer::parseInt)
                .collect(Collectors.toList());
        List<List<Integer>> nearbyTickets = input.subList(input.lastIndexOf("") + 2, input.size()).stream()
                .map(s -> Arrays.stream(s.split(",")).map(Integer::parseInt).collect(Collectors.toList()))
                .collect(Collectors.toList());

        CommonUtils.printPartHeader(1);

        TicketScanner ticketScanner = new TicketScanner(fieldsInput);
        int errorRate = 0;
        Iterator<List<Integer>> itr = nearbyTickets.iterator();
        while (itr.hasNext()) {
            List<Integer> ticket = itr.next();
            int error = ticketScanner.errorRate(ticket);
            if (error > 0) {
                errorRate += ticketScanner.errorRate(ticket);
                itr.remove();
            }
        }
        System.out.printf("Ticket scanning error rate: %d%n%n", errorRate);

        CommonUtils.printPartHeader(2);

        ticketScanner.determineFieldOrder(nearbyTickets);
        System.out.printf("Departure product: %d%n", ticketScanner.departureProduct(myTicket));
    }
}
