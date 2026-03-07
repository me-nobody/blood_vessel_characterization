import qupath.lib.objects.*
import qupath.lib.objects.classes.*
import qupath.lib.images.*
import qupath.lib.regions.*
import qupath.lib.gui.dialogs.*
import qupath.lib.scripting.*
import qupath.lib.analysis.*
import qupath.lib.measurements.*
import qupath.lib.images.exceptions.*
import qupath.lib.images.servers.*
import qupath.lib.gui.*
import qupath.lib.plugins.*
import qupath.lib.roi.*
import qupath.lib.util.*




print(getCurrentViewer())
print(getCurrentImageData())
print(getCurrentServer())


// Convert annotations to detections
def annotations = getAnnotationObjects().findAll{it.getPathClass()}
def newDetections = annotations.collect{
    return PathObjects.createDetectionObject(it.getROI(), it.getPathClass())
}
// removeObjects(annotations, true) // uncomment to delete original annotations
addObjects(newDetections)
print(newDetections.size())


// select detection objects
cells = getDetectionObjects()
selectObjects(cells)

// add measurements

addShapeMeasurements("AREA", 
                     "LENGTH", 
                     "CIRCULARITY",
                     "SOLIDITY", 
                     "MAX_DIAMETER",
                     "MIN_DIAMETER",
                     "NUCLEUS_CELL_RATIO")

runPlugin('qupath.lib.algorithms.IntensityFeaturesPlugin', '{"pixelSizeMicrons": 2.0, ' + 
                                                            '"region": "ROI", ' +
                                                            '"tileSizeMicrons": 25.0, ' +
                                                            '"colorOD": true, ' +
                                                            '"colorStain1": true, ' +
                                                            '"colorStain2": true, ' +
                                                            '"colorStain3": false, ' +
                                                            '"colorRed": true, ' +
                                                            '"colorGreen": true, ' +
                                                            '"colorBlue": true, ' +
                                                            '"colorHue": true, ' +
                                                            '"colorSaturation": true, ' +
                                                            '"colorBrightness": true, ' +
                                                            '"doMean": true, ' +
                                                            '"doStdDev": true, ' +
                                                            '"doMinMax": true, ' +
                                                            '"doMedian": true, ' +
                                                            '"doHaralick": false, ' +
                                                            '"haralickDistance": 1, ' +
                                                            '"haralickBins": 32}')



