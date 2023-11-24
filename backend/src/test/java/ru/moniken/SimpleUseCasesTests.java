package ru.moniken;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.moniken.dto.RouteCollectionDTO;
import ru.moniken.dto.RouteDTO;
import ru.moniken.exception.RouteCollectionNotFoundException;
import ru.moniken.model.entity.Route;
import ru.moniken.model.entity.RouteCollection;
import ru.moniken.repository.RouteCollectionRepository;
import ru.moniken.repository.RouteRepository;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleUseCasesTests {

    private static final String MONIKEN_ENDPOINT = "/moniken";
    private static final String COLLECTIONS_ENDPOINT = "/collections";
    private static final String ROUTES_ENDPOINT = "/routes";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    RouteCollectionRepository collectionRepository;

    @MockBean
    RouteRepository routeRepository;

    @Test
    public void givenIncorrectCollectionData_whenCreate_thenStatusBadRequest() throws Exception {
        RouteCollectionDTO newCollection = new RouteCollectionDTO();
        newCollection.setId("");
        newCollection.setName("");
        newCollection.setDescription("");

        String requestBody = objectMapper.writeValueAsString(newCollection);

        mockMvc.perform(post(MONIKEN_ENDPOINT + COLLECTIONS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        // Проверка, что объект не сохраняется в базу
        RouteCollection persistent = modelMapper.map(newCollection, RouteCollection.class);
        verify(collectionRepository, times(0)).save(persistent);
    }

    @Test
    public void givenCorrectCollectionData_whenCreate_thenStatusCreatedAndGetIsEqualCreated() throws Exception {
        RouteCollectionDTO newCollection = new RouteCollectionDTO();
        newCollection.setName("NewName");
        newCollection.setDescription("description");

        RouteCollection toPersist = modelMapper.map(newCollection, RouteCollection.class);
        RouteCollection persistentResult = modelMapper.map(newCollection, RouteCollection.class);
        persistentResult.setId("1");
        when(collectionRepository.save(toPersist)).thenReturn(persistentResult);

        String requestBody = objectMapper.writeValueAsString(newCollection);

        var postResponse = mockMvc.perform(
                        post(MONIKEN_ENDPOINT + COLLECTIONS_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        RouteCollectionDTO created = objectMapper.readValue(postResponse, RouteCollectionDTO.class);
        when(collectionRepository.findByName(created.getName())).thenReturn(Optional.of(persistentResult));

        var getResponse = mockMvc.perform(
                        get(MONIKEN_ENDPOINT + COLLECTIONS_ENDPOINT + "/" + created.getName()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RouteCollectionDTO get = objectMapper.readValue(getResponse, RouteCollectionDTO.class);
        assertEquals(created, get);
    }

    @Test
    public void givenNotExistedCollectionName_whenGet_returnNotFound() throws Exception {
        String notExistedCollectionName = "name";
        when(collectionRepository.findByName(notExistedCollectionName))
                .thenReturn(Optional.empty());

        mockMvc.perform(get(MONIKEN_ENDPOINT + COLLECTIONS_ENDPOINT + "/" + notExistedCollectionName))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenAlreadyExistedCollection_whenCreate_thenStatusConflict() throws Exception {
        RouteCollectionDTO requestData = new RouteCollectionDTO();
        requestData.setName("alreadyExistsCollection");
        requestData.setDescription("description");


        RouteCollection persistent = modelMapper.map(requestData, RouteCollection.class);
        when(collectionRepository.save(persistent))
                .thenThrow(new DataIntegrityViolationException("Unique index or primary key violation"));

        mockMvc.perform(
                        post(MONIKEN_ENDPOINT + COLLECTIONS_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isConflict());
    }

    @Test
    public void givenRoute_whenAddToNotExistedCollection_thenStatusNotFound() throws Exception {
        String notExistedCollectionName = "collection";
        when(collectionRepository.findByName(notExistedCollectionName))
                .thenThrow(new RouteCollectionNotFoundException(notExistedCollectionName));

        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setEndpoint("/correctEndpoint");

        mockMvc.perform(
                        post(MONIKEN_ENDPOINT + COLLECTIONS_ENDPOINT + "/" + notExistedCollectionName + ROUTES_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(routeDTO))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenCorrectRouteData_whenAdd_thenStatusCreated() throws Exception {
        String existedCollectionName = "collectionName";

        RouteDTO requestData = new RouteDTO();
        requestData.setEndpoint("/endpoint");
        requestData.setDescription("description");
        requestData.setName("name");

        RouteCollection routeCollection = new RouteCollection();
        routeCollection.setName(existedCollectionName);
        routeCollection.setId("testId");
        when(collectionRepository.findByName(existedCollectionName)).
                thenReturn(Optional.of(routeCollection));

        Route toPersist = modelMapper.map(requestData, Route.class);
        toPersist.setCollection(routeCollection);

        Route persistent = modelMapper.map(requestData, Route.class);
        persistent.setId("routeId");
        persistent.setEndpoint("repository");
        persistent.setCollection(routeCollection);

        when(routeRepository.save(toPersist)).thenReturn(persistent);

        mockMvc.perform(
                        post(MONIKEN_ENDPOINT + COLLECTIONS_ENDPOINT + "/" + existedCollectionName + ROUTES_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestData))
                )
                .andExpect(status().isCreated());

    }

    @Test
    public void givenIncorrectRouteData_whenAdd_thenStatusBadRequest() throws Exception {
        RouteDTO requestData = new RouteDTO();
        requestData.setEndpoint("endpoint");


        mockMvc.perform(
                        post(MONIKEN_ENDPOINT + COLLECTIONS_ENDPOINT + "/any" + ROUTES_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isBadRequest());

        Route toPersist = modelMapper.map(requestData, Route.class);
        verify(routeRepository, times(0)).save(toPersist);
    }

    @Test
    public void givenRoute_whenEndpointReservedForServicesNeeds_thenStatusConflict() throws Exception {
        // given
        RouteCollection routeCollection = new RouteCollection();
        routeCollection.setName("name");

        when(collectionRepository.findByName(routeCollection.getName())).thenReturn(Optional.of(routeCollection));

        RouteDTO requestData = new RouteDTO();
        requestData.setEndpoint("/moniken");
        mockMvc.perform(
                        post(MONIKEN_ENDPOINT + COLLECTIONS_ENDPOINT + "/" + routeCollection.getName() + ROUTES_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isConflict());

        Route toPersist = modelMapper.map(requestData, Route.class);
        verify(routeRepository, times(0)).save(toPersist);
    }

    @Test
    public void givenRoute_whenUnknownMethod_thenStatusBadRequest() throws Exception {
        String incorrectMethod = "213123";
        String requestData = "{\n    \"endpoint\":\"/endpoint\",\n    \"method\": \"" + incorrectMethod + "\"\n}";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept-Langauge", "en");
        mockMvc.perform(
                        post(MONIKEN_ENDPOINT + COLLECTIONS_ENDPOINT + "/any" + ROUTES_ENDPOINT)
                                .locale(Locale.US)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestData)
                ).andExpect(status().isBadRequest())
                .andExpect(content().string("{\"message\":\"" +
                        "Route method " + incorrectMethod + " is incorrect" +
                        "\"}"));

    }

}
