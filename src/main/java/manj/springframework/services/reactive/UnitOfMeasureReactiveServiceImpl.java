package manj.springframework.services.reactive;

import org.springframework.stereotype.Service;

import manj.springframework.commands.UnitOfMeasureCommand;
import manj.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import manj.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureReactiveServiceImpl implements UnitOfMeasureReactiveService {

	private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
	private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

	public UnitOfMeasureReactiveServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
			UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
		this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
	}

	@Override
	public Flux<UnitOfMeasureCommand> listAllUoms() {

		return unitOfMeasureReactiveRepository.findAll().map(unitOfMeasureToUnitOfMeasureCommand::convert);
	}
}
