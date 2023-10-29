package org.example.agents.buyer;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.tools.gui.ACLAIDDialog;
import org.example.containers.ConsumerContainer;

import java.awt.print.Book;

public class BookBuyerAgent extends GuiAgent {

     protected BookBuyerGui gui;

    @Override
    protected void setup() {
        if(getArguments().length==1)//test important si l'agent migre vers un autre container
        //il ne porte pas avec lui l'interface graphiqe
        {
            gui= (BookBuyerGui) getArguments()[0];
            gui.bookBuyerAgent=this;//lier l'agent a l'interface graphic (le cosumer container)
        }
        ParallelBehaviour PB=new ParallelBehaviour();
        addBehaviour(PB);
         PB.addSubBehaviour(
                new CyclicBehaviour()
                {
                    @Override
                    public void action() {
                        ACLMessage aclMessage=receive();
                        if(aclMessage!=null)
                        {
                            gui.logMessage(aclMessage);
                        }
                        else block();
                    }
                }
        );
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {

    }
}
