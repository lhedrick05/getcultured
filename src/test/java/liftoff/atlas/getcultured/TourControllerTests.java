package liftoff.atlas.getcultured;

import liftoff.atlas.getcultured.controllers.TourController;
import liftoff.atlas.getcultured.models.Tour;
import liftoff.atlas.getcultured.services.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


import java.util.Optional;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(TourController.class)
public class TourControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TourService tourService;

    @InjectMocks
    private TourController tourController;

    @BeforeEach
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(tourController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testCreateTourForm() throws Exception {
        mockMvc.perform(get("/tours/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("tours/create"))
                .andExpect(model().attributeExists("tour"));
    }

    @Test
    public void testCreateTour() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "filename.jpg", "image/jpeg", "some-image".getBytes());
        mockMvc.perform(multipart("/tours/create")
                        .file(file)
                        .param("name", "New Tour")
                        // Add other parameters...
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tours"));

        verify(tourService, times(1)).saveTour(any(Tour.class), any(MockMultipartFile.class));
    }

    @Test
    public void testListTours() throws Exception {
        when(tourService.getAllTours()).thenReturn(Arrays.asList(new Tour(), new Tour()));

        mockMvc.perform(get("/tours"))
                .andExpect(status().isOk())
                .andExpect(view().name("tours/tours"))
                .andExpect(model().attributeExists("tours"));
    }

    @Test
    public void testViewTour() throws Exception {
        Tour mockTour = new Tour();
        // Correctly returning an Optional of mockTour
        when(tourService.getTourById(anyInt())).thenReturn(mockTour);

        mockMvc.perform(get("/tours/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewTour"))
                .andExpect(model().attributeExists("tour"));
    }

    @Test
    public void testDeleteTour() throws Exception {
        mockMvc.perform(post("/tours/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tours"));
    }

}
