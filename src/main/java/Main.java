import venus.assembler.Assembler;
import venus.assembler.AssemblerError;
import venus.assembler.AssemblerOutput;
import venus.linker.LinkedProgram;
import venus.linker.Linker;
import venus.riscv.Program;
import venus.simulator.Simulator;
import venus.riscv.insts.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    
    private static void init() {
        AddiKt.getAddi();
        AddKt.getAdd();
        AndiKt.getAndi();
        AndKt.getAnd();
        AuipcKt.getAuipc();
        BeqKt.getBeq();
        BgeKt.getBge();
        BgeuKt.getBgeu();
        BltKt.getBlt();
        BltuKt.getBltu();
        BneKt.getBne();
        DivKt.getDiv();
        DivuKt.getDivu();
        EcallKt.getEcall();
        JalKt.getJal();
        JalrKt.getJalr();
        LbKt.getLb();
        LbuKt.getLbu();
        LhKt.getLh();
        LhuKt.getLhu();
        LuiKt.getLui();
        LwKt.getLw();
        MulhKt.getMulh();
        MulhsuKt.getMulhsu();
        MulhuKt.getMulhu();
        MulKt.getMul();
        OriKt.getOri();
        OrKt.getOr();
        RemKt.getRem();
        RemuKt.getRemu();
        SbKt.getSb();
        ShKt.getSh();
        SlliKt.getSlli();
        SllKt.getSll();
        SltiKt.getSlti();
        SltiuKt.getSltiu();
        SltKt.getSlt();
        SltuKt.getSltu();
        SraiKt.getSrai();
        SraKt.getSra();
        SrliKt.getSrli();
        SrlKt.getSrl();
        SubKt.getSub();
        SwKt.getSw();
        XoriKt.getXori();
        XorKt.getXor();
    }
    
    private static byte[] readAllBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return new byte[]{};
    }
    
    private static void checkForErrors(AssemblerOutput assemblerOutput) {
        List<AssemblerError> errors = assemblerOutput.getErrors();
        if (errors.isEmpty()) return;
        errors.forEach(System.err::println);
        System.exit(1);
    }
    
    public static void main(String[] args) {
        init();
        List<String> stringPrograms = Arrays.stream(args).peek(System.out::println).map(Paths::get).map(Main::readAllBytes).map(String::new).collect(Collectors.toList());
        List<Program> programs = stringPrograms.stream().map(Assembler.INSTANCE::assemble).peek(Main::checkForErrors).map(AssemblerOutput::getProg).collect(Collectors.toList());
        LinkedProgram linkedProgram = Linker.INSTANCE.link(programs);
        run(linkedProgram);
    }
    
    private static void run(LinkedProgram program) {
        Simulator simulator = new Simulator(program);
        simulator.run();
//        System.out.println(simulator.getReg(17));
    }
    
}
