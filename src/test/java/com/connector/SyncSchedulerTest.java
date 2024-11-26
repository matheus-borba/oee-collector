// package com.connector;

// import static org.mockito.Mockito.doThrow;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// import com.connector.scheduler.SyncScheduler;
// import com.connector.service.ApiService;

// import io.quarkus.test.junit.QuarkusTest;

// @QuarkusTest
// public class SyncSchedulerTest {

// 	@Mock
// 	ApiService service;

// 	@InjectMocks
// 	SyncScheduler syncScheduler;

// 	@BeforeEach
// 	public void setup() {
// 		MockitoAnnotations.openMocks(this);
// 	}

// 	@Test
// 	public void testSyncronizeMachines() throws Exception {
// 		syncScheduler.syncronizeAllMachines();

// 		verify(service, times(1)).syncronizeAllMachines();
// 	}

// 	@Test
// 	public void testSyncronizeMachinesThrowsException() throws Exception {
// 		doThrow(new RuntimeException("Error"))
// 				.when(service).syncronizeAllMachines();

// 		syncScheduler.syncronizeAllMachines();

// 		verify(service, times(1)).syncronizeAllMachines();
// 	}

// 	@Test
// 	public void testSyncronizeProductions() throws Exception {
// 		syncScheduler.syncronizeAllProductions();

// 		verify(service, times(1)).syncronizeAllProduction();
// 	}

// 	@Test
// 	public void testSyncronizeProductionsThrowsException() throws Exception {
// 		doThrow(new RuntimeException("Error"))
// 				.when(service).syncronizeAllProduction();

// 		syncScheduler.syncronizeAllProductions();

// 		verify(service, times(1)).syncronizeAllProduction();
// 	}
// }
