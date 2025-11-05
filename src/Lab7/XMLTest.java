package Lab7;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface XMLComponent {
    void addAttribute(String name, String value);
    String print(int indent);
}

abstract class XMLElement implements XMLComponent {

    protected String type;
    protected Map<String, String> attributes;

    public XMLElement(String type) {
        this.type = type;
        attributes = new LinkedHashMap<>();
    }

    @Override
    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }
}

class XMLLeaf extends XMLElement implements XMLComponent {

    private String value;

    public XMLLeaf(String type, String value) {
        super(type);
        this.value = value;
    }

    @Override
    public String print(int indent) {
        return String.format("%s<%s%s>%s</%s>",
                IntStream.range(0, indent).mapToObj(i->"\t").collect(Collectors.joining("")),
                type,
                attributes.entrySet().stream()
                        .map(s->String.format(" %s=\"%s\"", s.getKey(), s.getValue()))
                        .collect(Collectors.joining("")),value,type);
    }
}

class XMLComposite extends XMLElement implements XMLComponent {

    private List<XMLComponent> children;

    public XMLComposite(String type) {
        super(type);
        children = new ArrayList<>();
    }

    @Override
    public String print(int indent) {
        String tabs = IntStream.range(0,indent).mapToObj(i->"\t").collect(Collectors.joining(""));
        return String.format("%s<%s %s>\n%s\n%s</%s>",tabs,type,
                attributes.entrySet().stream()
                        .map(s->String.format("%s=\"%s\"", s.getKey(), s.getValue()))
                        .collect(Collectors.joining(" ")),
                children.stream().map(i->i.print(indent+1)).collect(Collectors.joining("\n")),
                tabs,type);
    }

    public void addComponent(XMLComponent child) {
        children.add(child);
    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase==1) {
            //TODO Print the component object
            System.out.println(component.print(0));
        } else if(testCase==2) {
            //TODO print the composite object
            System.out.println(composite.print(0));
        } else if (testCase==3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level","1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level","2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level","3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            //TODO print the main object

            System.out.println(main.print(0));
        }
    }
}
