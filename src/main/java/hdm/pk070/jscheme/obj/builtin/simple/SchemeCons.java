package hdm.pk070.jscheme.obj.builtin.simple;

import hdm.pk070.jscheme.obj.SchemeObject;

import java.util.Objects;

/**
 * This class poses a JScheme list. In Scheme, every list is made up of one or more cons cells. In other words, lists
 * in Scheme are implemented as linked lists.
 *
 * @author patrick.kleindienst
 */
public final class SchemeCons extends SchemeObject {

    private final SchemeObject car;
    private final SchemeObject cdr;

    public SchemeCons(final SchemeObject car, final SchemeObject cdr) {
        Objects.requireNonNull(car);
        Objects.requireNonNull(cdr);
        this.car = car;
        this.cdr = cdr;
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("SchemeCons does not have a value, use getCar() and getCdr()");
    }

    public SchemeObject getCar() {
        return car;
    }

    public SchemeObject getCdr() {
        return cdr;
    }


    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("'(");
        stringBuffer.append(prettyPrintList(this));
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    private String prettyPrintList(SchemeObject schemeObject) {
        String listPrint = "";

        if (schemeObject.typeOf(SchemeCons.class)) {
            listPrint += ((SchemeCons) schemeObject).getCar().toString();
            listPrint += " ";
            return (listPrint + prettyPrintList(((SchemeCons) schemeObject).getCdr())).trim();
        } else if (schemeObject.typeOf(SchemeNil.class)) {
            return listPrint;
        } else {
            return (listPrint + ". " + schemeObject.toString());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        if (!this.getCar().equals(((SchemeCons) obj).getCar())) {
            return false;
        }
        return this.getCdr().equals(((SchemeCons) obj).getCdr());
    }

    @Override
    public int hashCode() {
        int hashCode = 5;
        if (Objects.nonNull(car)) {
            hashCode = (hashCode * 31) + car.hashCode();
        }
        if (Objects.nonNull(cdr)) {
            hashCode = (hashCode * 31) + cdr.hashCode();
        }
        return hashCode;
    }
}
