package util.ui

import java.awt.{BorderLayout, Dimension}
import javax.swing.{JFrame, JScrollPane, JTextArea}

class ConsoleUi(name: String, onCommand: String => Unit) {

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
    onCommand(command)
  }

  def appendStatus(status: String) = {
    output.append(status + "\n")
    scrollableOutput.getVerticalScrollBar.setValue(scrollableOutput.getVerticalScrollBar.getMaximum)
    this
  }
}
