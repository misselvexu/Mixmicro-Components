package xyz.vopen.mixmicro.components.mongo.client.geo;

import java.util.List;

interface GeometryFactory {
  Geometry createGeometry(List<?> geometries);
}
