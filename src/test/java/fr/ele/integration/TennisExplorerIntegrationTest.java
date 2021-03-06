package fr.ele.integration;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.ele.services.mapping.TennisExplorerSynchroniser;

public class TennisExplorerIntegrationTest extends
		AbstractSuperbetIntegrationTest {
	@Autowired
	private TennisExplorerSynchroniser tennisExplorerSynchroniser;

	@Override
	@Before
	public void initializeDatas() {
		super.initializeDatas();
	}

	@Test
	public void testParse() {
		InputStream inputStream = new BufferedInputStream(
				WIlliamHillIntegrationTest.class
						.getResourceAsStream("/fr/ele/feeds/williamhill/WilliamHill.xml"));
		/**
		 * Oxip odds = tennisExplorerSynchroniser.unmarshall(inputStream, null);
		 * tennisExplorerSynchroniser.synchronize("tennisexplorer", odds);
		 */
	}
}