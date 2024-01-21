package Processing_Unit

import Register_Unit.ProgramCounter
import chisel3._
import chisel3.util._

class ProcessorUnit(wordSize: Int, opSize: Int, sel1Size: Int, sel2Size: Int) extends Module{
    val io = IO(new Bundle{
        val memWord = Input(UInt(wordSize.W))
        val readAddr2 = Input(UInt(5.W))
        val writeAddr = Input(UInt(5.W))
        val loadR0, loadR1, loadR2, loadR3, loadPC, incPC = Input(Bool())
        val selBus1Mux = Input(UInt(sel1Size.W))
        val selBus2Mux = Input(UInt(sel2Size.W))
        val loadIR, loadAddR, loadRegY, loadRegZ = Input(Bool())

        val instruction = Output(UInt(wordSize.W))
        val address = Output(UInt(wordSize.W))
        val bus1 = Output(UInt(wordSize.W))
        val zFlag = Output(Bool())
    })

    val Bus2 = Wire(UInt(wordSize.W))
    val R0_out, R1_out, R2_out, R3_out = Wire(UInt(wordSize.W))
    val PC_count, ALU_out = Wire(UInt(wordSize.W))
    val ALU_Z_flag = Wire(Bool())
    val opcode = io.instruction(wordSize-1,wordSize - opSize)

    val R0 = RegEnable(Bus2, 0.U,io.loadR0)
    val R1 = RegEnable(Bus2, 0.U,io.loadR1)
    val R2 = RegEnable(Bus2, 0.U,io.loadR2)
    val R3 = RegEnable(Bus2, 0.U,io.loadR3)
    val Y_val = RegEnable(Bus2, 0.U,io.loadRegY)
    val io.zFlag = RegEnable(ALU_Z_flag, Bool(), io.loadRegZ)
    val io.address = RegEnable(Bus2, 0.U, io.loadAddR)
    val io.instruction = RegEnable(Bus2, 0.U, io.loadIR)
    val PC = (new ProgramCounter(wordSize))
    PC.io.dataIn := Bus2
    PC.io.loadPC := io.loadPC
    PC.io.incPC := io.incPC

    io.bus1 := 0.U(wordSize.W)

    switch(io.selBus1Mux){
        is(0.U(sel1Size.W)){
            io.bus1 := R0
        }
        is(1.U(sel1Size.W)){
            io.bus1 := R1
        }
        is(2.U(sel1Size.W)){
            io.bus1 := R2
        }
        is(3.U(sel1Size.W)){
            io.bus1 := R3
        }
        is(4.U(sel1Size.W)){
            io.bus1 := PC.io.count
        }
    }

    switch(io.selBus2Mux){
        is(0.U(sel2Size.W)){
            Bus2 := ALU.io.ALU_out
        }
        is(1.U(sel2Size.W)){
            Bus2 := io.bus1
        }
        is(2.U(sel2Size.W)){
            Bus2 := io.memWord
        }
    }

    val ALU = (new ALU(wordSize, opSize))
    ALU.io.data1 := Y_val
    ALU.io.data2 := io.bus1
    ALU.io.opcode := opcode
    ALU.io.zFlag := ALU_Z_flag

}
