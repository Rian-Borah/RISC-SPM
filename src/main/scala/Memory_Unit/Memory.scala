package Memory_Unit

import chisel3._
import chisel3.util._

/**
 * wordSize: 8-bit Int
 * memSize: 8-bit Int
 *
 * io.addr: Address bus; 8-bit UInt Input
 * io.dataIn: Input Data bus; 8-bit UInt Input
 * io.dataOut: Output Data bus; 8-bit UInt Output
 * io.memWrite: Enable write; Bool Input
 * io.memRead: Enable read; Bool Input
 */
class Memory(wordSize: Int, memSize: Int) extends Module {
    val io = IO(new Bundle {
        val addr: UInt = Input(UInt(wordSize.W))
        val dataIn: UInt = Input(UInt(wordSize.W))
        val dataOut: UInt = Output(UInt(wordSize.W))
        val memWrite: Bool = Input(Bool())
        val memRead: Bool = Input(Bool())
    })

    val mem = Mem(memSize, UInt(wordSize.W))

    when(io.memWrite) {
        mem(io.addr) := io.dataIn
    }

    io.dataOut := 0.U
    when(io.memRead) {
        io.dataOut := mem(io.addr)
    }
}