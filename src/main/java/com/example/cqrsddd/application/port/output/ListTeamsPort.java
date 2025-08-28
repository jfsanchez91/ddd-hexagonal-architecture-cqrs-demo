package com.example.cqrsddd.application.port.output;

import com.example.cqrsddd.application.port.input.ListTeamsQuery;
import java.util.Collection;

public interface ListTeamsPort {
    Collection<ListTeamsQuery.Projection> execute(ListTeamsQuery.Query query);
}
