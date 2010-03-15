/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.components.barbecue.BarbecueCompiler;
import net.sf.jasperreports.components.barbecue.BarbecueDesignConverter;
import net.sf.jasperreports.components.barbecue.BarbecueFillFactory;
import net.sf.jasperreports.components.barcode4j.BarcodeCompiler;
import net.sf.jasperreports.components.barcode4j.BarcodeDesignConverter;
import net.sf.jasperreports.components.barcode4j.BarcodeFillFactory;
import net.sf.jasperreports.components.list.FillListFactory;
import net.sf.jasperreports.components.list.ListComponent;
import net.sf.jasperreports.components.list.ListComponentCompiler;
import net.sf.jasperreports.components.list.ListDesignConverter;
import net.sf.jasperreports.components.table.FillTableFactory;
import net.sf.jasperreports.components.table.TableCompiler;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.component.DefaultComponentManager;
import net.sf.jasperreports.engine.component.DefaultComponentXmlParser;
import net.sf.jasperreports.engine.component.DefaultComponentsBundle;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;

/**
 * Extension registry factory that includes built-in component element
 * implementations.
 * 
 * <p>
 * This registry factory is registered by default in JasperReports.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 * @see ListComponent
 */
public class ComponentsExtensionsRegistryFactory implements
		ExtensionsRegistryFactory
{

	public static final String NAMESPACE = 
		"http://jasperreports.sourceforge.net/jasperreports/components";
	public static final String XSD_LOCATION = 
		"http://jasperreports.sourceforge.net/xsd/components.xsd";
	public static final String XSD_RESOURCE = 
		"net/sf/jasperreports/components/components.xsd";
	
	protected static final String LIST_COMPONENT_NAME = "list";
	protected static final String TABLE_COMPONENT_NAME = "table";
	protected static final String BARBECUE_COMPONENT_NAME = "barbecue";
	protected static final String[] BARCODE4J_COMPONENT_NAMES = 
		new String[]{"Codabar", "Code128", "EAN128", "DataMatrix", "Code39", "Interleaved2Of5",
		"UPCA", "UPCE", "EAN13", "EAN8", "USPSIntelligentMail", "RoyalMailCustomer", 
		"POSTNET", "PDF417"};
	
	private static final ExtensionsRegistry REGISTRY;
	
	static
	{
		final DefaultComponentsBundle bundle = new DefaultComponentsBundle();

		ComponentsXmlHandler xmlHandler = new ComponentsXmlHandler();
		
		DefaultComponentXmlParser parser = new DefaultComponentXmlParser();
		parser.setNamespace(NAMESPACE);
		parser.setPublicSchemaLocation(XSD_LOCATION);
		parser.setInternalSchemaResource(XSD_RESOURCE);
		parser.setDigesterConfigurer(xmlHandler);
		bundle.setXmlParser(parser);
		
		HashMap componentManagers = new HashMap();
		
		DefaultComponentManager listManager = new DefaultComponentManager();
		listManager.setDesignConverter(new ListDesignConverter());
		listManager.setComponentCompiler(new ListComponentCompiler());
		listManager.setComponentXmlWriter(xmlHandler);
		listManager.setComponentFillFactory(new FillListFactory());
		componentManagers.put(LIST_COMPONENT_NAME, listManager);
		
		DefaultComponentManager tableManager = new DefaultComponentManager();
		tableManager.setComponentCompiler(new TableCompiler());
		tableManager.setComponentXmlWriter(xmlHandler);
		tableManager.setComponentFillFactory(new FillTableFactory());
		componentManagers.put(TABLE_COMPONENT_NAME, tableManager);
		
		DefaultComponentManager barbecueManager = new DefaultComponentManager();
		barbecueManager.setDesignConverter(new BarbecueDesignConverter());
		barbecueManager.setComponentCompiler(new BarbecueCompiler());
		barbecueManager.setComponentXmlWriter(xmlHandler);
		barbecueManager.setComponentFillFactory(new BarbecueFillFactory());
		componentManagers.put(BARBECUE_COMPONENT_NAME, barbecueManager);
		
		DefaultComponentManager barcode4jManager = new DefaultComponentManager();
		barcode4jManager.setDesignConverter(new BarcodeDesignConverter());
		barcode4jManager.setComponentCompiler(new BarcodeCompiler());
		barcode4jManager.setComponentXmlWriter(xmlHandler);
		barcode4jManager.setComponentFillFactory(new BarcodeFillFactory());
		for (int i = 0; i < BARCODE4J_COMPONENT_NAMES.length; i++)
		{
			componentManagers.put(BARCODE4J_COMPONENT_NAMES[i], barcode4jManager);
		}
		
		bundle.setComponentManagers(componentManagers);
		
		REGISTRY = new ExtensionsRegistry()
		{
			public List getExtensions(Class extensionType)
			{
				if (ComponentsBundle.class.equals(extensionType))
				{
					return Collections.singletonList(bundle);
				}
				
				return null;
			}
		};
	}
	
	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties)
	{
		return REGISTRY;
	}

}
