package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.domain.Instrument;
import com.gitlab.amirmehdi.repository.InstrumentRepository;
import com.gitlab.amirmehdi.service.crawler.OmidRLCConsumer;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class InstrumentService {
    private final InstrumentRepository instrumentRepository;
    private final OmidRLCConsumer omidRLCConsumer;

    public InstrumentService(InstrumentRepository instrumentRepository, @Lazy OmidRLCConsumer omidRLCConsumer) {
        this.instrumentRepository = instrumentRepository;
        this.omidRLCConsumer = omidRLCConsumer;
    }

    public List<Instrument> findAll() {
        return instrumentRepository.findAll();
    }

    public Optional<Instrument> findOneByName(String darayi) {
        Optional<Instrument> optionalInstrument = instrumentRepository.findOneByName(darayi);
        if (!optionalInstrument.isPresent()) {
            List<com.gitlab.amirmehdi.service.dto.core.Instrument> instruments = omidRLCConsumer.searchInstrument(darayi);
            if (instruments.isEmpty()) {
                return optionalInstrument;
            }
            com.gitlab.amirmehdi.service.dto.core.Instrument apiInstrument;
            if (instruments.stream().anyMatch(instrument -> instrument.getName().equals(darayi))) {
                apiInstrument = instruments.stream().filter(instrument -> instrument.getName().equals(darayi)).collect(Collectors.toList()).get(0);
            } else {
                apiInstrument = instruments.get(0);
            }
            Instrument instrument = new Instrument()
                .isin(apiInstrument.getId())
                .name(darayi)
                .tseId(apiInstrument.getTseId())
                .updatedAt(LocalDate.now());
            save(instrument);
            return Optional.of(instrument);
        }
        return optionalInstrument;
    }

    private void save(Instrument instrument) {
        log.info("instrument save name: {}", instrument.getName());
        instrumentRepository.save(instrument);
    }

    public List<Instrument> findAllInstrumentHasNotOption() {
        return instrumentRepository.findAllInstrumentHasNotOption();
    }
}
