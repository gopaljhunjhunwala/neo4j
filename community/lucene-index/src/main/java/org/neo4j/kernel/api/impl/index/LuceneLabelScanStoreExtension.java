/**
 * Copyright (c) 2002-2013 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.api.impl.index;

import java.io.File;

import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.extension.KernelExtensionFactory;
import org.neo4j.kernel.impl.api.scan.LabelScanStoreProvider;
import org.neo4j.kernel.impl.nioneo.store.FileSystemAbstraction;
import org.neo4j.kernel.impl.transaction.XaDataSourceManager;
import org.neo4j.kernel.logging.Logging;

import static org.neo4j.kernel.api.impl.index.IndexWriterFactories.standard;
import static org.neo4j.kernel.api.impl.index.LuceneKernelExtensions.directoryFactory;
import static org.neo4j.kernel.api.impl.index.LuceneLabelScanStore.loggerMonitor;
import static org.neo4j.kernel.impl.api.scan.LabelScanStoreProvider.fullStoreLabelUpdateStream;

public class LuceneLabelScanStoreExtension extends KernelExtensionFactory<LuceneLabelScanStoreExtension.Dependencies>
{
    public interface Dependencies
    {
        Config getConfig();

        FileSystemAbstraction getFileSystem();
        
        XaDataSourceManager getDataSourceManager();
        
        Logging getLogging();
    }
    
    public LuceneLabelScanStoreExtension()
    {
        super( "lucene");
    }
    
    @Override
    public LabelScanStoreProvider newKernelExtension( Dependencies dependencies ) throws Throwable
    {
        DirectoryFactory directoryFactory = directoryFactory( dependencies.getConfig(), dependencies.getFileSystem() );
        File storeDir = dependencies.getConfig().get( GraphDatabaseSettings.store_dir );
        LuceneLabelScanStore scanStore = new LuceneLabelScanStore(
                new LuceneDocumentStructure(),
                
                // <db>/schema/label/lucene
                directoryFactory, new File( new File( new File( storeDir, "schema" ), "label" ), "lucene" ),
                
                dependencies.getFileSystem(), standard(),
                fullStoreLabelUpdateStream( dependencies.getDataSourceManager() ),
                loggerMonitor( dependencies.getLogging() ) );
        
        return new LabelScanStoreProvider( scanStore, 10 );
    }
}
