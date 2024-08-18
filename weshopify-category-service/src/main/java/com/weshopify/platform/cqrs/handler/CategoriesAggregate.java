package com.weshopify.platform.cqrs.handler;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.weshopify.platform.cqrs.commands.CategoryCommand;
import com.weshopify.platform.cqrs.domainevents.CategoryEvent;

import lombok.extern.slf4j.Slf4j;

@Aggregate
@Slf4j
public class CategoriesAggregate {

	@AggregateIdentifier
	private String id;
	private String name;
	private String alias;
	private Integer pcategory;
	private boolean enabled;

	@CommandHandler
	public CategoriesAggregate(CategoryCommand command) {
		log.info("Step-2: Command Handler Recieved the command and Creating an Event");
		CategoryEvent event = CategoryEvent.builder().name(command.getName()).alias(command.getAlias())
				.pcategory(command.getPcategory()).enabled(command.isEnabled()).id(command.getId()).build();
		
		log.info("Step-3: Publishing the Created Event to the Event Handlers");

		AggregateLifecycle.apply(event);
	}
	
	@EventSourcingHandler
	public void on(CategoryEvent event) {
		log.info("Step-4: Event Handler Recieved the Event");
		this.id = event.getId();
		this.name = event.getName();
		this.pcategory = event.getPcategory();
		this.alias = event.getAlias();
		this.enabled = event.isEnabled();
	}
}
