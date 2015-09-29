package util.ui

import java.awt.event._
import javax.swing.JTextField

class CommandLine(onCommand: (String) => Unit) extends JTextField{

  private var lastCommand = ""

  addActionListener(new ActionListener() {
    def actionPerformed(e:ActionEvent) = {
      lastCommand = getText
      setText("")

      onCommand(lastCommand)
    }
  })

  addKeyListener(new KeyAdapter() {
    override def keyPressed(e: KeyEvent): Unit =
      if (e.getKeyCode == KeyEvent.VK_UP)
        setText(lastCommand)
  })
}
