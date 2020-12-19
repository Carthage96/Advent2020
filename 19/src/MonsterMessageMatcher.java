import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MonsterMessageMatcher {
    private final List<Rule> ruleList;

    public MonsterMessageMatcher(List<String> allRules) {
        ruleList = new ArrayList<>(allRules.size());
        for (int i = 0; i < allRules.size(); i++) {
            ruleList.add(new Rule(i));
        }
        for (int i = 0; i < ruleList.size(); i++) {
            String ruleString = allRules.get(i);
            int ruleNum = Integer.parseInt(ruleString.substring(0, ruleString.indexOf(":")));
            Rule rule = ruleList.get(ruleNum);
            if (ruleString.contains("\"")) {
                rule.setLiteral(ruleString.charAt(ruleString.indexOf("\"") + 1));
            } else {
                for (String subRuleString : ruleString.substring(ruleString.indexOf(":") + 1).split("\\|")) {
                    List<Rule> subRule = Arrays.stream(subRuleString.strip().split(" "))
                            .map(s -> getRule(Integer.parseInt(s))).collect(Collectors.toList());
                    rule.addSubRule(subRule);
                }
            }
        }
    }

    public boolean match(String s, int rule) {
        return matchFull(s, Collections.singletonList(ruleList.get(rule)), 0);
    }

    private boolean matchFull(String s, List<Rule> rulesToMatch, int start) {
        if (start == s.length() || rulesToMatch.size() == 0) {
            return start == s.length() && rulesToMatch.size() == 0;
        }
        Rule currentRule = rulesToMatch.get(0);
        if (currentRule.isLiteral()) {
            if (s.charAt(start) == currentRule.getLiteral()) {
                return matchFull(s, rulesToMatch.subList(1, rulesToMatch.size()), start + 1);
            } else {
                return false;
            }
        } else {
            for (List<Rule> subRule : currentRule.rules) {
                List<Rule> newRules = new ArrayList<>(subRule.size() + rulesToMatch.size() - 1);
                newRules.addAll(subRule);
                newRules.addAll(rulesToMatch.subList(1, rulesToMatch.size()));
                if (matchFull(s, newRules, start)) {
                    return true;
                }
            }
            return false;
        }
    }

    private Rule getRule(int index) {
        return ruleList.get(index);
    }

    private static class Rule {
        private final int name;
        public final List<List<Rule>> rules;
        private Character literal;

        public Rule(int name) {
            this.name = name;
            rules = new ArrayList<>(2);
            literal = null;
        }

        @Override
        public String toString() {
            return "Rule{" +
                    name +
                    '}';
        }

        public void setLiteral(Character literal) {
            this.literal = literal;
            rules.clear();
        }

        public Character getLiteral() {
            return literal;
        }

        public void addSubRule(List<Rule> newRule) {
            rules.add(newRule);
            literal = null;
        }

        public boolean isLiteral() {
            return literal != null;
        }
    }
}
