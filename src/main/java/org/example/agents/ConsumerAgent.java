package org.example.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import org.example.containers.ConsumerContainer;

public class ConsumerAgent extends GuiAgent {
    private ConsumerContainer gui;
    @Override
    protected void setup() {
        if(getArguments().length==1)//test important si l'agent migre vers un autre container
                                    //il ne porte pas avec lui l'interface graphiqe
        {
            gui= (ConsumerContainer) getArguments()[0];
            gui.setConsumeragent(this);//lier l'agent a l'interface graphic (le cosumer container)
        }

        System.out.println(" agent initialisé : "+this.getAID().getName());
        if(this.getArguments().length==1)
        {
            System.out.println("je vais tenter d'acheter le livre : "+getArguments()[0]);
        }
        ParallelBehaviour PB=new ParallelBehaviour();
        addBehaviour(PB);

        PB.addSubBehaviour(
                new CyclicBehaviour() {
                    @Override
                    public void action() {
                        ACLMessage aclmessage=receive();
                        if(aclmessage!=null)
                        {
                            System.out.println("Reception du message");
                            System.out.println( aclmessage.getContent());
                            System.out.println(aclmessage.getSender().getName());
                            System.out.println(aclmessage.getPerformative());
                            System.out.println(aclmessage.getLanguage());

                            ACLMessage reply= aclmessage.createReply();
                            reply.setContent("hii i'm replying");
                            send(reply);
                        }
                        else
                            block();
                    }
                }
        );
    }

    @Override
    public void doDelete() {
    }


    @Override
    protected void beforeMove() {
    }

    @Override
    protected void afterMove() {
    }

    @Override
    public void onGuiEvent(GuiEvent params) {
        if(params.getType()==1)
        {
            String livre=params.getParameter(0).toString();
            ACLMessage aclMessage=new ACLMessage(ACLMessage.REQUEST);
            aclMessage.setContent(livre);
            aclMessage.addReceiver(new AID("BookBuyerAgent",AID.ISLOCALNAME));
            send(aclMessage);
        }
    }

}
