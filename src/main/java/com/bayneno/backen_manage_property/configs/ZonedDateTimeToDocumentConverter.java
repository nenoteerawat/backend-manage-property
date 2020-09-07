package com.bayneno.backen_manage_property.configs;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.lang.Nullable;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static java.util.Arrays.asList;

@Component
@WritingConverter
public class ZonedDateTimeToDocumentConverter implements Converter<ZonedDateTime, Document> {
    static final String DATE_TIME = "dateTime";
    static final String ZONE = "zone";

    @Override
    public Document convert(@Nullable ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) return null;

        Document document = new Document();
        document.put(DATE_TIME, Date.from(zonedDateTime.toInstant()));
        document.put(ZONE, zonedDateTime.getZone().getId());
        document.put("offset", zonedDateTime.getOffset().toString());
        return document;
    }
}

@Component
@ReadingConverter
class DocumentToZonedDateTimeConverter implements Converter<Document, ZonedDateTime> {

    @Override
    public ZonedDateTime convert(@Nullable Document document) {
        if (document == null) return null;

        Date dateTime = document.getDate(ZonedDateTimeToDocumentConverter.DATE_TIME);
        String zoneId = document.getString(ZonedDateTimeToDocumentConverter.ZONE);
        ZoneId zone = ZoneId.of(zoneId);

        return ZonedDateTime.ofInstant(dateTime.toInstant(), zone);
    }
}

@Configuration
@ComponentScan(basePackageClasses = {DocumentToZonedDateTimeConverter.class, ZonedDateTimeToDocumentConverter.class})
class MongoConfiguration{

    @Bean
    public MongoClientOptions mongoClientOptions() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                .automatic(true)
                .build();

        CodecRegistry registry = CodecRegistries.fromRegistries(
                createCustomCodecRegistry(),
                MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(pojoCodecProvider)
        );

        return MongoClientOptions.builder()
                .codecRegistry(registry)
                .build();
    }

    private CodecRegistry createCustomCodecRegistry() {
        return CodecRegistries.fromCodecs(
                new ZonedDateTimeCodec()
        );
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(asList(
                new ZonedDateTimeToDocumentConverter(),
                new DocumentToZonedDateTimeConverter()
        ));
    }
}
