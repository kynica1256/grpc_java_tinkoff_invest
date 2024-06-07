package ru.tinkoff;

class Methods_calc {
    public float data_;
    public void  Methods_calc_(long Units, long Nano, String figi) {
        String value_main = "";
        if (figi == "BBG004730ZJ9") {
            value_main = value_main.concat("0.0");
        }
        String units = Long.toString(Units);
        String nano = Long.toString(Nano);
        nano = nano.replaceAll("0+$", "");
        value_main = value_main.concat(units);
        value_main = value_main.concat(".");
        if (Nano == 0) {
            value_main = value_main.concat("0");
        } else {
            value_main = value_main.concat(nano);
        }
        data_ = Float.parseFloat(value_main);
    }
}
