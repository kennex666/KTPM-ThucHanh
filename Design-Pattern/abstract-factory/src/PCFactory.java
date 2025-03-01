public class PCFactory implements ComputerAbstractFactory{
    private String ram;
    private String hdd;
    private String cpu;

    public PCFactory(String ram, String cpu, String hdd) {
        this.ram = ram;
        this.cpu = cpu;
        this.hdd = hdd;
    }

    @Override
    public Computer createComputer() {
        return new PC(ram, cpu, hdd);
    }
}
