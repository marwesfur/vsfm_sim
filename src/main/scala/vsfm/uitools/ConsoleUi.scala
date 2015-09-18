package vsfm.uitools

import java.awt.{Dimension, BorderLayout}
import javax.swing.{JScrollPane, JFrame, JTextArea}

class ConsoleUi(name: String, onCommand: PartialFunction[String, Unit]) {

  private val commandLine = new CommandLine(onCommandInternal)
  private val output = new JTextArea { setEditable(false) }
  private val scrollableOutput = new JScrollPane(output)
  private val frame = new JFrame(name) {
    getContentPane.add(scrollableOutput, BorderLayout.CENTER)
    getContentPane.add(commandLine, BorderLayout.SOUTH)
    getContentPane.setPreferredSize(new Dimension(300,200))
    pack()
    setLocationByPlatform(true)
    setVisible(true)
  }

  private def onCommandInternal(command: String) = {
    output.append("> " + command + "\n")
    onCommand.orElse(beep)(command)
  }

  private def beep: PartialFunction[String, Unit] = {
    case failedCommand => appendStatus("beep")
  }

  def appendStatus(status: String) = {
    output.append(status + "\n")
    scrollableOutput.getVerticalScrollBar.setValue(scrollableOutput.getVerticalScrollBar.getMaximum)
    this
  }
}
